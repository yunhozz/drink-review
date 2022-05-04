package drinkreview.domain.review;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.DrinkRepository;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.domain.review.dto.ReviewRequestDto;
import drinkreview.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Review review = this.findReview(reviewId);

        if (!review.getMember().getId().equals(userId)) {
            throw new IllegalStateException("You do not have permission.");
        }

        review.updateField(dto.getTitle(), dto.getContent(), dto.getScore());
    }

    public void deleteReview(Long reviewId, Long userId) {
        Review review = this.findReview(reviewId);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));

        //어드민 권한 or 게시글 작성자만 삭제 가능
        if (member.getAuthorities().contains("ADMIN") || review.getMember().getId().equals(member.getId())) {
            reviewRepository.delete(review); //cascade : delete Comment

        } else {
            throw new IllegalStateException("You do not have permission.");
        }
    }

    @Transactional(readOnly = true)
    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Review is null."));
    }

    @Transactional(readOnly = true)
    public List<Review> findReviewList() {
        return reviewRepository.findAll();
    }
}
