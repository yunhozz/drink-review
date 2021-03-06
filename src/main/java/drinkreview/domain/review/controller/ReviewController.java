package drinkreview.domain.review.controller;

import drinkreview.domain.comment.dto.CommentChildResponseDto;
import drinkreview.domain.comment.dto.CommentRequestDto;
import drinkreview.domain.comment.dto.CommentResponseDto;
import drinkreview.domain.drink.repository.DrinkRepository;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.review.ReviewService;
import drinkreview.domain.review.dto.ReviewRequestDto;
import drinkreview.domain.review.dto.ReviewResponseDto;
import drinkreview.global.ui.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/community/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final DrinkRepository drinkRepository;

    @GetMapping("/{id}")
    public String read(@LoginMember MemberSessionResponseDto loginMember, @PathVariable("id") Long reviewId, @ModelAttribute CommentRequestDto commentRequestDto,
                       HttpServletRequest request, HttpServletResponse response, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }
        model.addAttribute("loginMember", loginMember);

        ReviewResponseDto review = reviewService.findReviewDto(reviewId);
        List<CommentResponseDto> comments = review.getComments();
        List<List<CommentChildResponseDto>> commentChildBigList = new ArrayList<>();

        for (CommentResponseDto commentResponseDto : comments) {
            List<CommentChildResponseDto> commentChildList = commentResponseDto.getCommentChildList();
            commentChildBigList.add(commentChildList);
        }
        model.addAttribute("review", review);
        model.addAttribute("comments", comments);
        model.addAttribute("commentChildBigList", commentChildBigList);
        viewCountUp(reviewId, request, response); //????????? ??????

        String drinkName = drinkRepository.findDrinkNameWithReviewId(reviewId)
                .orElseThrow(() -> new IllegalStateException("Can't find drink's name."));
        model.addAttribute("drinkName", drinkName);

        //review ???????????? update&delete ?????? ??????
        if (review.getUserId().equals(loginMember.getId())) {
            model.addAttribute("reviewWriter", true);
        }

        return "review/review-detail";
    }

    @GetMapping("/write")
    public String write(@LoginMember MemberSessionResponseDto loginMember, @ModelAttribute ReviewRequestDto reviewRequestDto, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }
        model.addAttribute("loginMember", loginMember);

        List<String> drinkNames = drinkRepository.findDrinkNamesInOrder(loginMember.getId());
        model.addAttribute("drinkNames", drinkNames);

        return "review/write";
    }

    @PostMapping("/write")
    public String write(@LoginMember MemberSessionResponseDto loginMember, @Valid ReviewRequestDto reviewRequestDto, BindingResult result, HttpServletRequest request) {
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
    public String update(@LoginMember MemberSessionResponseDto loginMember, @PathVariable("id") Long reviewId, @ModelAttribute ReviewRequestDto reviewRequestDto,
                         Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        ReviewResponseDto review = reviewService.findReviewDto(reviewId);
        model.addAttribute("review", review);

        return "review/update";
    }

    @PostMapping("/{id}/update")
    public String update(@LoginMember MemberSessionResponseDto loginMember, @PathVariable("id") Long reviewId, @Valid ReviewRequestDto reviewRequestDto,
                         BindingResult result) {
        if (result.hasErrors()) {
            return "review/update";
        }

        reviewService.updateReview(reviewRequestDto, reviewId, loginMember.getId());
        return "redirect:/community";
    }

    @GetMapping("/{id}/delete")
    public String delete(@LoginMember MemberSessionResponseDto loginMember, @PathVariable("id") Long reviewId) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        reviewService.deleteReview(reviewId, loginMember.getId());
        return "redirect:/community";
    }

    //????????? ?????? + ?????? ??????
    private void viewCountUp(Long reviewId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("review")) {
                    oldCookie = cookie;
                }
            }
        }
        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + reviewId.toString() + "]")) {
                reviewService.addView(reviewId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + reviewId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            reviewService.addView(reviewId);
            Cookie newCookie = new Cookie("review","[" + reviewId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }
}
