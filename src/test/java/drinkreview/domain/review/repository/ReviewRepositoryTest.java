package drinkreview.domain.review.repository;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.DrinkRepository;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.domain.review.Review;
import drinkreview.domain.review.dto.ReviewQueryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired DrinkRepository drinkRepository;
    @Autowired EntityManager em;
    Member member;
    Drink drink;

    @BeforeEach
    void beforeEach() {
        member = createMember("qkrdbsgh", "111", "yunho");
        em.persist(member);

        drink = createDrink("drink");
        em.persist(drink);
    }

    @Test
    void exist() throws Exception {
        //given
        Review review = createReview(member, drink, "review", "content", 1);

        //when
        reviewRepository.save(review);
        Boolean result1 = reviewRepository.exist(review.getId());
        Boolean result2 = reviewRepository.exist(100L);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void searchList() throws Exception {
        //given
        Review review1 = createReview(member, drink, "review1", "content1", 1);
        Review review2 = createReview(member, drink, "review2", "content2", 2);
        Review review3 = createReview(member, drink, "review3", "content3", 3);

        //when
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        List<ReviewQueryDto> result = reviewRepository.searchList();

        //then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).extracting("title")
                .containsExactly("review3", "review2", "review1");
    }

    @Test
    void searchPageByScoreOrder() throws Exception {
        //given
        for (int i = 1; i <= 10; i++) {
            Review review = createReview(member, drink, "review" + i, "content" + i, (10-i));
            reviewRepository.save(review);

            Thread.sleep(5);
        }

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        Page<ReviewQueryDto> result = reviewRepository.searchPageByScoreOrder(pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result).extracting("title")
                .containsExactly("review1", "review2", "review3");
    }

    @Test
    void searchPageByDateOrder() throws Exception {
        //given
        for (int i = 1; i <= 10; i++) {
            Review review = createReview(member, drink, "review" + i, "content" + i, i);
            reviewRepository.save(review);

            Thread.sleep(5);
        }

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        Page<ReviewQueryDto> result = reviewRepository.searchPageByScoreOrder(pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result).extracting("title")
                .containsExactly("review10", "review9", "review8");
    }

    private Review createReview(Member member, Drink drink, String title, String content, double score) {
        return Review.builder()
                .member(member)
                .drink(drink)
                .title(title)
                .content(content)
                .score(score)
                .build();
    }

    private Member createMember(String memberId, String memberPw, String name) {
        return Member.builder()
                .memberId(memberId)
                .memberPw(memberPw)
                .name(name)
                .build();
    }

    private Drink createDrink(String name) {
        return Drink.builder()
                .name(name)
                .build();
    }
}