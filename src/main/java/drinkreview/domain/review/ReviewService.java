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

    public Long makeReview(ReviewRequestDto reviewRequestDto, Long userId, Long drinkId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));

        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));

        reviewRequestDto.setMember(member);
        reviewRequestDto.setDrink(drink);
        Review review = reviewRepository.save(reviewRequestDto.toEntity());

        return review.getId();
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
