package drinkreview.domain.review;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.dto.ReviewQueryDto;
import drinkreview.domain.review.repository.ReviewRepository;
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
    @Autowired EntityManager em;
    Member member;
    Drink drink;

    @BeforeEach
    void beforeEach() {
        member = createMember("qkrdbsgh", "111", "yunho", "ADMIN");
        em.persist(member);

        drink = createDrink("drink");
        em.persist(drink);
    }

    @Test
    void findWithKeyword() throws Exception {
        //given
        Review review1 = createReview(member, drink, "ABCfind", "content", member.getName(), 1);
        Review review2 = createReview(member, drink, "review", "findABC", member.getName(), 2);
        Review review3 = createReview(member, drink, "ABCfindABC", "ABCfindABC", member.getName(), 3);
        Review review4 = createReview(member, drink, "title", "content", member.getName(), 4);
        Review review5 = createReview(member, drink, "title", "content", member.getName(), 5);

        //when
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);
        reviewRepository.save(review5);

        List<Review> result = reviewRepository.findWithKeyword("find");

        //then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).contains(review1, review2, review3);
        assertThat(result).doesNotContain(review4, review5);
    }

    @Test
    void addView() throws Exception {
        //given
        Review review = createReview(member, drink, "title", "content", member.getName(), 100);

        //when
        reviewRepository.save(review);
        reviewRepository.addView(review.getId());

        //then
        assertThat(review.getView()).isEqualTo(0); //DB 에서 조회한 값(view = 1)을 버리고 영속성 컨텍스트에서 조회한 값(view = 0)을 반환함
    }

    @Test
    void isMemberNull() throws Exception {
        //given
        Review review1 = createReview(member, drink, "ABCfind", "content", member.getName(), 1);
        Review review2 = createReview(null, drink, "review", "findABC", member.getName(), 2);

        //when
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        boolean result1 = reviewRepository.isMemberNull(review1.getId());
        boolean result2 = reviewRepository.isMemberNull(review2.getId());

        //then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
    }

    @Test
    void exist() throws Exception {
        //given
        Review review = createReview(member, drink, "review", "content", member.getName(), 1);

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
        Review review1 = createReview(member, drink, "review1", "content1", member.getName(), 1);
        Review review2 = createReview(member, drink, "review2", "content2", member.getName(), 2);
        Review review3 = createReview(member, drink, "review3", "content3", member.getName(), 3);

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
            Review review = createReview(member, drink, "review" + i, "content" + i, member.getName(), (10-i));
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
            Review review = createReview(member, drink, "review" + i, "content" + i, member.getName(), i);
            reviewRepository.save(review);

            Thread.sleep(10);
        }

        Review findReview = reviewRepository.findById(10L).get();
        findReview.updateField("update", "update", 100); //업데이트

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        Page<ReviewQueryDto> result = reviewRepository.searchPageByDateOrder(pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result).extracting("title")
                .containsExactly("review10", "review9", "update");
    }

    @Test
    void searchPageByKeyword() throws Exception {
        //given
        for (int i = 1; i <= 5; i++) {
            Review review = createReview(member, drink, "find" + i, "content" + i, member.getName(), i);
            reviewRepository.save(review);
            Thread.sleep(5);
        }

        for (int i = 6; i <= 10; i++) {
            Review review = createReview(member, drink, "review" + i, "find" + i, member.getName(), i);
            reviewRepository.save(review);
            Thread.sleep(5);
        }

        for (int i = 11; i <= 15; i++) {
            Review review = createReview(member, drink, "find" + i, "find" + i, member.getName(), i);
            reviewRepository.save(review);
            Thread.sleep(5);
        }

        for (int i = 16; i <= 20; i++) {
            Review review = createReview(member, drink, "review" + i, "content" + i, member.getName(), i);
            reviewRepository.save(review);
            Thread.sleep(5);
        }

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<ReviewQueryDto> result = reviewRepository.searchPageDateByKeyword("find", pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result).extracting("title")
                .containsExactly("find15", "find14", "find13", "find12", "find11",
                        "review10", "review9", "review8", "review7", "review6");
    }

    private Review createReview(Member member, Drink drink, String title, String content, String memberName, double score) {
        return Review.builder()
                .member(member)
                .drink(drink)
                .title(title)
                .content(content)
                .memberName(memberName)
                .score(score)
                .build();
    }

    private Member createMember(String memberId, String memberPw, String name, String auth) {
        return Member.builder()
                .memberId(memberId)
                .memberPw(memberPw)
                .name(name)
                .auth(auth)
                .build();
    }

    private Drink createDrink(String name) {
        return Drink.builder()
                .name(name)
                .build();
    }
}