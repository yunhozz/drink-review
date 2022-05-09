package drinkreview.api;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.comment.repository.CommentRepository;
import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.DrinkRepository;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.domain.review.Review;
import drinkreview.domain.review.dto.ReviewQueryDto;
import drinkreview.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final DrinkRepository drinkRepository;

    @GetMapping("/review/{id}")
    public ReviewQueryDto findReview(@PathVariable("id") Long reviewId) {
        return reviewRepository.findReview(reviewId);
    }

    @GetMapping("/review/list")
    public List<ReviewQueryDto> reviewList() {
        return reviewRepository.searchList();
    }

    @GetMapping("/review/list/score")
    public Page<ReviewQueryDto> reviewByScore(Pageable pageable) {
        return reviewRepository.searchPageByScoreOrder(pageable);
    }

    @GetMapping("/review/list/date")
    public Page<ReviewQueryDto> reviewByDate(Pageable pageable) {
        return reviewRepository.searchPageByDateOrder(pageable);
    }

    @GetMapping("/review/list/view")
    public Page<ReviewQueryDto> reviewByView(Pageable pageable) {
        return reviewRepository.searchPageByViewOrder(pageable);
    }

    @GetMapping("/review/list/search")
    public Page<ReviewQueryDto> reviewByKeyword(@RequestParam("word") String keyword, Pageable pageable) {
        return reviewRepository.searchPageByKeyword(keyword, pageable);
    }

//    @PostConstruct
    public void init() throws Exception {
        Member member = new Member("qkrdbsgh", "111", 27);
        memberRepository.save(member);

        Drink drink = Drink.builder().name("drink").build();
        drinkRepository.save(drink);

        for (int i = 1; i <= 10; i++) {
            Review review = Review.builder()
                    .member(member)
                    .drink(drink)
                    .title("review" + i)
                    .content("content" + i)
                    .score(i)
                    .build();

            reviewRepository.save(review);

            for (int j = 1; j <= 5; j++) {
                Comment comment = Comment.createComment(member, review, "comment" + j);
                commentRepository.save(comment);
                Thread.sleep(5);
            }

            Thread.sleep(5);
        }

        for (int i = 11; i <= 20; i++) {
            Review review = Review.builder()
                    .member(member)
                    .drink(drink)
                    .title("review" + i)
                    .content("find" + i)
                    .score(i)
                    .build();

            reviewRepository.save(review);

            for (int j = 1; j <= 5; j++) {
                Comment comment = Comment.createComment(member, review, "comment" + j);
                commentRepository.save(comment);
                Thread.sleep(5);
            }

            Thread.sleep(5);
        }

        for (int i = 21; i <= 30; i++) {
            Review review = Review.builder()
                    .member(member)
                    .drink(drink)
                    .title("review" + i)
                    .content("content" + i)
                    .score(i)
                    .build();

            reviewRepository.save(review);

            for (int j = 1; j <= 5; j++) {
                Comment comment = Comment.createComment(member, review, "comment" + j);
                commentRepository.save(comment);
                Thread.sleep(5);
            }

            Thread.sleep(5);
        }
    }
}
