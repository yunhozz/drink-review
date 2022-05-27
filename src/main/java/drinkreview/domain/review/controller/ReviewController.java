package drinkreview.domain.review.controller;

import drinkreview.domain.drink.DrinkService;
import drinkreview.domain.drink.dto.DrinkSimpleResponseDto;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.review.ReviewService;
import drinkreview.domain.review.dto.ReviewRequestDto;
import drinkreview.domain.review.dto.ReviewResponseDto;
import drinkreview.global.controller.SessionConstant;
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
    private final DrinkService drinkService;

    @GetMapping("/review")
    public String read(@RequestParam("id") Long reviewId, Model model) {
        ReviewResponseDto review = reviewService.findReviewDto(reviewId);
        model.addAttribute("review", review);

        return "review/detail";
    }

    @GetMapping("/review/write")
    public String write(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember,
                        @ModelAttribute ReviewRequestDto reviewDto, Model model) {
        if (loginMember == null) {
            return "member/login";
        }
        model.addAttribute("loginMember", loginMember);

        List<DrinkSimpleResponseDto> drinks = drinkService.findDrinkSimpleDtoList();
        model.addAttribute("drinks", drinks);

        return "review/write";
    }

    @PostMapping("/review/write")
    public String write(@Valid ReviewRequestDto reviewDto, BindingResult result, @RequestParam("userId") Long userId,
                        @RequestParam("drinkId") Long drinkId) {
        if (result.hasErrors()) {
            return "review/write";
        }

        reviewService.makeReview(reviewDto, userId, drinkId);
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
