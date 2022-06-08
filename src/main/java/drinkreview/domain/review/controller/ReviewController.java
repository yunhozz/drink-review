package drinkreview.domain.review.controller;

import drinkreview.domain.drink.repository.DrinkRepository;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/community/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final DrinkRepository drinkRepository;

    @GetMapping("/{id}")
    public String read(@PathVariable("id") Long reviewId, @SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, Model model) {
        if (loginMember == null) {
            return "member/login";
        }

        ReviewResponseDto review = reviewService.findReviewDto(reviewId);
        reviewService.addView(review.getId());
        model.addAttribute("review", review);

        String drinkName = drinkRepository.findDrinkNameWithReviewId(reviewId)
                .orElseThrow(() -> new IllegalStateException("Can't find drink's name."));
        model.addAttribute("drinkName", drinkName);

        //review, comment 작성자만 update 버튼 표시
        if (review.getUserId().equals(loginMember.getId())) {
            model.addAttribute("reviewWriter", true);
        }
        for (int i = 0; i < review.getComments().size(); i++) {
            boolean isCommentWriter = review.getComments().get(i).getUserId().equals(loginMember.getId());
            model.addAttribute("commentWriter", isCommentWriter);
        }

        return "review/review-detail";
    }

    @GetMapping("/write")
    public String write(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @ModelAttribute ReviewRequestDto reviewRequestDto,
                        Model model) {
        if (loginMember == null) {
            return "member/login";
        }
        model.addAttribute("loginMember", loginMember);

        List<String> drinkNames = drinkRepository.findDrinkNamesInOrder(loginMember.getId());
        model.addAttribute("drinkNames", drinkNames);

        return "review/write";
    }

    @PostMapping("/write")
    public String write(@Valid ReviewRequestDto reviewRequestDto, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "review/write";
        }

        HttpSession session = request.getSession();
        MemberSessionResponseDto loginMember = (MemberSessionResponseDto) session.getAttribute(SessionConstant.LOGIN_MEMBER);

        String drinkName = request.getParameter("drinkName");
        Long drinkId = drinkRepository.findDrinkIdWithName(drinkName)
                .orElseThrow(() -> new IllegalStateException("Can't find drink with this name : " + drinkName));

        reviewService.makeReview(reviewRequestDto, loginMember.getId(), drinkId);
        return "redirect:/community";
    }

    @GetMapping("/{id}/update")
    public String update(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @PathVariable("id") Long reviewId,
                         @ModelAttribute ReviewRequestDto reviewRequestDto, Model model) {
        if (loginMember == null) {
            return "member/login";
        }

        ReviewResponseDto review = reviewService.findReviewDto(reviewId);
        model.addAttribute("review", review);

        return "review/update";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long reviewId, @Valid ReviewRequestDto reviewRequestDto, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "review/update";
        }

        HttpSession session = request.getSession();
        MemberSessionResponseDto loginMember = (MemberSessionResponseDto) session.getAttribute(SessionConstant.LOGIN_MEMBER);
        reviewService.updateReview(reviewRequestDto, reviewId, loginMember.getId());

        return "redirect:/community";
    }

    @GetMapping("/{id}/delete")
    public String delete(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @PathVariable("id") Long reviewId) {
        if (loginMember == null) {
            return "member/login";
        }

        reviewService.deleteReview(reviewId, loginMember.getId());
        return "redirect:/community";
    }
}
