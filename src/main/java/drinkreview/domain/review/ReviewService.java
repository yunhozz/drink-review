package drinkreview.domain.review;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.DrinkRepository;
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

        dto.setMember(member);
        dto.setDrink(drink);
        Review review = reviewRepository.save(dto.toEntity());

        return review.getId();
    }

    public void updateReview(ReviewRequestDto dto, Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Review is null."));

        if (!review.getMember().getId().equals(userId)) {
            throw new IllegalStateException("You do not have permission.");
        }

        review.updateField(dto.getTitle(), dto.getContent(), dto.getScore());
    }

    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Review is null."));

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));

        //게시글 작성자만 삭제 가능
        if (review.getMember().getId().equals(member.getId())) {
            reviewRepository.delete(review); //cascade : delete Comment

        } else {
            throw new IllegalStateException("You do not have permission.");
        }
    }

    public void updateView(Long reviewId) {
        reviewRepository.addView(reviewId);
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto findReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Review is null."));

        return new ReviewResponseDto(review);
    }
}
