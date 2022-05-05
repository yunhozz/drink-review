package drinkreview.domain.review;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.drink.DrinkService;
import drinkreview.domain.drink.dto.DrinkRequestDto;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.member.service.MemberService;
import drinkreview.domain.review.dto.ReviewRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;
    private final DrinkService drinkService;

    @GetMapping("/review")
    public String reviewApi() {
        return "redirect:/api/review/date";
    }

    @GetMapping("/review/write")
    public String writeForm(Model model) {
        model.addAttribute("memberDto", new MemberRequestDto());
        model.addAttribute("drinkDto", new DrinkRequestDto());
        model.addAttribute("reviewDto", new ReviewRequestDto());

        return "review/write";
    }

    @PostMapping("/review/write")
    public String write(MemberSessionResponseDto memberSessionResponseDto) {
        return "/review/list";
    }

    @GetMapping("/review/read/{id}")
    public String readForm(@PathVariable Long reviewId, @Valid MemberSessionResponseDto memberSessionResponseDto,
                           HttpServletRequest request, Model model) {

        Review review = reviewService.findReview(reviewId);
        List<Comment> comments = review.getComments();

        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        HttpSession session = request.getSession();
        MemberSessionResponseDto member = (MemberSessionResponseDto) session.getAttribute("memberId");

        if (member != null) {
            model.addAttribute("memberId", member.getMemberId());

            if (review.getMember().getId().equals(member.getId())) {
                model.addAttribute("writer", true);
            }
        }

        reviewService.updateView(reviewId);

        return "review/read";
    }
}
