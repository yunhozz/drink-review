package drinkreview.domain.review.controller;

import drinkreview.domain.drink.repository.DrinkRepository;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.review.ReviewService;
import drinkreview.domain.review.dto.ReviewRequestDto;
import drinkreview.domain.review.dto.ReviewResponseDto;
import drinkreview.domain.review.repository.ReviewRepository;
import drinkreview.global.controller.LoginSessionConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final DrinkRepository drinkRepository;

    @GetMapping("/review/{id}")
    public String read(@PathVariable("id") Long reviewId, Model model) {
        ReviewResponseDto review = reviewService.findReviewDto(reviewId);
        reviewRepository.addView(review.getId());
        model.addAttribute("review", review);

        return "review/review-detail";
    }

    @GetMapping("/review/write")
    public String write(@SessionAttribute(LoginSessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @ModelAttribute ReviewRequestDto reviewRequestDto,
                        Model model) {
        if (loginMember == null) {
            return "member/login";
        }
        model.addAttribute("loginMember", loginMember);

        List<String> drinkNames = drinkRepository.findDrinkNamesInOrder(loginMember.getId());
        model.addAttribute("drinkNames", drinkNames);

        return "review/write";
    }

    @PostMapping("/review/write")
    public String write(@Valid ReviewRequestDto reviewRequestDto, BindingResult result, @RequestParam("userId") Long userId, @RequestParam("drinkId") Long drinkId) {
        if (result.hasErrors()) {
            return "review/write";
        }

        reviewService.makeReview(reviewRequestDto, userId, drinkId);
        return "redirect:/community";
    }

    @GetMapping("/review/{id}/update")
    public String update(@SessionAttribute(LoginSessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @PathVariable("id") Long reviewId,
                         @ModelAttribute ReviewRequestDto reviewRequestDto, Model model) {
        if (loginMember == null) {
            return "member/login";
        }

        return "review/write";
    }

    @PostMapping("/review/{id}/update")
    public String update(@PathVariable("id") Long reviewId, @Valid ReviewRequestDto reviewRequestDto, BindingResult result, @RequestParam("userId") Long userId) {
        if (result.hasErrors()) {
            return "review/write";
        }

        reviewService.updateReview(reviewRequestDto, reviewId, userId);
        return "redirect:/community";
    }

    @GetMapping("/review/{id}/delete")
    public String delete(@SessionAttribute(LoginSessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @PathVariable("id") Long reviewId) {
        if (loginMember == null) {
            return "member/login";
        }

        reviewService.deleteReview(reviewId, loginMember.getId());
        return "redirect:/community";
    }

    @PostConstruct
    public void init() throws Exception {
        for (int i = 1; i <= 20; i++) {
            ReviewRequestDto dto = new ReviewRequestDto();
            dto.setTitle("title" + i);
            dto.setContent("content" + i);
            dto.setScore((double) i);

            reviewService.makeReview(dto, 31L, Integer.toUnsignedLong(i));
            Thread.sleep(5);
        }
    }
}
