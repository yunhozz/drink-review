package drinkreview.domain.review;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.repository.DrinkRepository;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.domain.review.dto.ReviewRequestDto;
import drinkreview.domain.review.dto.ReviewResponseDto;
import drinkreview.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final DrinkRepository drinkRepository;

    public Long makeReview(ReviewRequestDto dto, Long userId, Long drinkId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));

        drinkRepository.updateGpa(drink.getId(), dto.getScore());
        drinkRepository.updateEvaluationCount(drink.getId());

        dto.setMember(member);
        dto.setDrink(drink);
        Review review = reviewRepository.save(dto.toEntity());

        return review.getId();
    }

    public void updateReview(ReviewRequestDto dto, Long reviewId, Long userId) {
        Review review = this.findReview(reviewId);
        if (!review.getMember().getId().equals(userId)) {
            throw new IllegalStateException("You do not have permission.");
        }
        review.updateField(dto.getTitle(), dto.getContent(), dto.getScore());
    }

    public void deleteReview(Long reviewId, Long userId) {
        Review review = this.findReview(reviewId);
        if (reviewRepository.isMemberNull(review.getId())) {
            throw new IllegalStateException("Only the admin has permission.");
        } else {
            //게시글 작성자만 삭제 가능
            if (review.getMember().getId().equals(userId)) {
                reviewRepository.delete(review); //cascade : delete Comment
            } else {
                throw new IllegalStateException("You do not have permission.");
            }
        }
    }

    public void addView(Long reviewId) {
        Review review = this.findReview(reviewId);
        reviewRepository.addView(review.getId());
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto findReviewDto(Long reviewId) {
        Review review = this.findReview(reviewId);
        return new ReviewResponseDto(review);
    }

    @Transactional(readOnly = true)
    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Review is null."));
    }
}
