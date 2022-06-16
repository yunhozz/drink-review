package drinkreview.domain.review.controller;

import drinkreview.domain.comment.dto.CommentChildResponseDto;
import drinkreview.domain.comment.dto.CommentRequestDto;
import drinkreview.domain.comment.dto.CommentResponseDto;
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
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final DrinkRepository drinkRepository;

    @GetMapping("/{id}")
    public String read(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember,
                       @PathVariable("id") Long reviewId, @ModelAttribute CommentRequestDto commentRequestDto, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        ReviewResponseDto review = reviewService.findReviewDto(reviewId);
        reviewService.addView(review.getId());
        model.addAttribute("review", review);
        model.addAttribute("comments", review.getComments());

        String drinkName = drinkRepository.findDrinkNameWithReviewId(reviewId)
                .orElseThrow(() -> new IllegalStateException("Can't find drink's name."));
        model.addAttribute("drinkName", drinkName);

        //review 작성자만 update&delete 버튼 표시
        if (review.getUserId().equals(loginMember.getId())) {
            model.addAttribute("reviewWriter", true);
        }

        //comment, commentChild 작성자만 update&delete 버튼 표시
        List<CommentResponseDto> comments = review.getComments();
        Map<List<CommentChildResponseDto>, Integer> map = new HashMap<>();
        for (CommentResponseDto comment : comments) {
            boolean isCommentWriter = comment.getUserId().equals(loginMember.getId());
            model.addAttribute("commentWriter", isCommentWriter);

            List<CommentChildResponseDto> commentChildList = comment.getCommentChildList();
            for (CommentChildResponseDto commentChildResponseDto : commentChildList) {
                boolean isCommentChildWriter = commentChildResponseDto.getUserId().equals(loginMember.getId());
                model.addAttribute("commentChildWriter", isCommentChildWriter);
            }
        }

        return "review/review-detail";
    }

    @GetMapping("/write")
    public String write(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember,
                        @ModelAttribute ReviewRequestDto reviewRequestDto, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }
        model.addAttribute("loginMember", loginMember);

        List<String> drinkNames = drinkRepository.findDrinkNamesInOrder(loginMember.getId());
        model.addAttribute("drinkNames", drinkNames);

        return "review/write";
    }

    @PostMapping("/write")
    public String write(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @Valid ReviewRequestDto reviewRequestDto,
                        BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "review/write";
        }

        String drinkName = request.getParameter("drinkName");
        Long drinkId = drinkRepository.findDrinkIdWithName(drinkName)
                .orElseThrow(() -> new IllegalStateException("Can't find drink with this name : " + drinkName));

        reviewService.makeReview(reviewRequestDto, loginMember.getId(), drinkId);
        return "redirect:/community";
    }

    @GetMapping("/{id}/update")
    public String update(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember,
                         @PathVariable("id") Long reviewId, @ModelAttribute ReviewRequestDto reviewRequestDto, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        ReviewResponseDto review = reviewService.findReviewDto(reviewId);
        model.addAttribute("review", review);

        return "review/update";
    }

    @PostMapping("/{id}/update")
    public String update(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @PathVariable("id") Long reviewId,
                         @Valid ReviewRequestDto reviewRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "review/update";
        }

        reviewService.updateReview(reviewRequestDto, reviewId, loginMember.getId());
        return "redirect:/community";
    }

    @GetMapping("/{id}/delete")
    public String delete(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember,
                         @PathVariable("id") Long reviewId) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        reviewService.deleteReview(reviewId, loginMember.getId());
        return "redirect:/community";
    }
}
