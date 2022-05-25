package drinkreview.api;

import drinkreview.domain.review.Review;
import drinkreview.domain.review.dto.ReviewQueryDto;
import drinkreview.domain.review.dto.ReviewResponseDto;
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

    @GetMapping("/reviews")
    public Page<ReviewResponseDto> findPage(Pageable pageable) {
        Page<Review> page = reviewRepository.findPage(pageable);
        Page<ReviewResponseDto> pageDto = page.map(ReviewResponseDto::new);

        return pageDto;
    }

    @GetMapping("/review/{id}")
    public ReviewQueryDto selectReview(@PathVariable("id") Long reviewId) {
        return reviewRepository.selectReview(reviewId);
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

    @GetMapping("/review/list/search/date")
    public Page<ReviewQueryDto> reviewDateByKeyword(@RequestParam("word") String keyword, Pageable pageable) {
        return reviewRepository.searchPageDateByKeyword(keyword, pageable);
    }

    @GetMapping("/review/list/search/accuracy")
    public Page<ReviewQueryDto> reviewAccuracyByKeyword(@RequestParam("word") String keyword, Pageable pageable) {
        return reviewRepository.searchPageAccuracyByKeyword(keyword, pageable);
    }
}
