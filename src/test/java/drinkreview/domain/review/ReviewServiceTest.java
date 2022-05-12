package drinkreview.domain.review;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.dto.ReviewRequestDto;
import drinkreview.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired ReviewService reviewService;
    @Autowired ReviewRepository reviewRepository;
    @Autowired EntityManager em;
    Member member;
    Drink drink;

    @BeforeEach
    void beforeEach() {
        member = Member.builder()
                .memberId("qkrdbsgh")
                .memberPw("111")
                .name("yunho")
                .age(27)
                .auth("ADMIN")
                .build();

        em.persist(member);

        drink = Drink.builder()
                .name("cola")
                .country("Korea")
                .build();

        em.persist(drink);
    }

    @Test
    void makeReview() throws Exception {
        //given
        ReviewRequestDto reviewDto1 = createReviewDto("title1", "content1", 2.6);
        ReviewRequestDto reviewDto2 = createReviewDto("title2", "content2", 5.8);
        ReviewRequestDto reviewDto3 = createReviewDto("title3", "content3", 9.2);

        //when
        Long reviewId1 = reviewService.makeReview(reviewDto1, member.getId(), drink.getId());
        Long reviewId2 = reviewService.makeReview(reviewDto2, member.getId(), drink.getId());
        Long reviewId3 = reviewService.makeReview(reviewDto3, member.getId(), drink.getId());

        Review review1 = reviewRepository.findById(reviewId1).get();
        Review review2 = reviewRepository.findById(reviewId2).get();
        Review review3 = reviewRepository.findById(reviewId3).get();

        //then
        assertThat(reviewId1).isEqualTo(review1.getId());
        assertThat(reviewId2).isEqualTo(review2.getId());
        assertThat(reviewId3).isEqualTo(review3.getId());
        assertThat(review1.getDrink()).isEqualTo(drink);
    }

    @Test
    void updateReview() throws Exception {
        //given
        ReviewRequestDto reviewDto = createReviewDto("title", "content", 9.2);

        //when
        Long reviewId = reviewService.makeReview(reviewDto, member.getId(), drink.getId());

        ReviewRequestDto dto = new ReviewRequestDto();
        dto.setTitle("update");
        dto.setContent("update");
        dto.setScore(10);
        reviewService.updateReview(dto, reviewId, member.getId());

        Review review = reviewRepository.findById(reviewId).get();

        //then
        assertThat(review.getTitle()).isEqualTo("update");
        assertThat(review.getContent()).isEqualTo("update");
        assertThat(review.getScore()).isEqualTo(10);
    }

    @Test
    void updateReviewFail() throws Exception {
        //given
        ReviewRequestDto reviewDto = createReviewDto("title", "content", 9.2);
        Member stranger = Member.builder()
                .memberId("stranger")
                .memberPw("stranger")
                .name("yanho")
                .build();

        em.persist(stranger);

        //when
        Long reviewId = reviewService.makeReview(reviewDto, member.getId(), drink.getId());

        ReviewRequestDto dto = new ReviewRequestDto();
        dto.setTitle("update");
        dto.setContent("update");
        dto.setScore(10);

        //then
        try {
            reviewService.updateReview(dto, reviewId, stranger.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteReview() throws Exception {
        //given
        ReviewRequestDto reviewDto1 = createReviewDto("title1", "content1", 2.6);
        ReviewRequestDto reviewDto2 = createReviewDto("title2", "content2", 5.8);
        ReviewRequestDto reviewDto3 = createReviewDto("title3", "content3", 9.2);

        Member newMember = Member.builder()
                .memberId("qkrdbsgh1121")
                .memberPw("222")
                .name("park yunho")
                .age(27)
                .auth("USER")
                .build();

        em.persist(newMember);

        //when
        Long reviewId1 = reviewService.makeReview(reviewDto1, member.getId(), drink.getId());
        Long reviewId2 = reviewService.makeReview(reviewDto2, newMember.getId(), drink.getId());
        Long reviewId3 = reviewService.makeReview(reviewDto3, member.getId(), drink.getId());

        reviewService.deleteReview(reviewId2, newMember.getId());

        Optional<Review> review1 = reviewRepository.findById(reviewId1);
        Optional<Review> review2 = reviewRepository.findById(reviewId2);
        Optional<Review> review3 = reviewRepository.findById(reviewId3);

        //then
        assertThat(review2).isEmpty();
        assertThat(review1).isNotEmpty();
        assertThat(review3).isNotEmpty();
    }

    @Test
    void deleteReviewFail() throws Exception {
        //given
        ReviewRequestDto reviewDto1 = createReviewDto("title1", "content1", 2.6);
        ReviewRequestDto reviewDto2 = createReviewDto("title2", "content2", 5.8);
        ReviewRequestDto reviewDto3 = createReviewDto("title3", "content3", 9.2);

        Member newMember = Member.builder()
                .memberId("qkrdbsgh1121")
                .memberPw("222")
                .name("park yunho")
                .age(27)
                .auth("USER")
                .build();

        em.persist(newMember);

        //when
        Long reviewId1 = reviewService.makeReview(reviewDto1, member.getId(), drink.getId());
        Long reviewId2 = reviewService.makeReview(reviewDto2, newMember.getId(), drink.getId());
        Long reviewId3 = reviewService.makeReview(reviewDto3, member.getId(), drink.getId());

        //then
        try {
            reviewService.deleteReview(reviewId2, member.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateView() throws Exception {
        //given
        ReviewRequestDto reviewDto = createReviewDto("title", "content", 9.2);

        //when
        Long reviewId = reviewService.makeReview(reviewDto, member.getId(), drink.getId());

        for (int i = 0; i < 5; i++) {
            reviewService.updateView(reviewId);
        }

        Review review = reviewRepository.findById(reviewId).get();

        //then
        assertThat(review.getView()).isEqualTo(5);
    }

    private ReviewRequestDto createReviewDto(String title, String content, double score) {
        ReviewRequestDto dto = new ReviewRequestDto();
        dto.setTitle(title);
        dto.setContent(content);
        dto.setScore(score);

        return dto;
    }
}