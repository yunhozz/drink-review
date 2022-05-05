package drinkreview.domain.review;

import drinkreview.domain.comment.dto.CommentResponseDto;
import drinkreview.domain.drink.DrinkService;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.member.service.MemberService;
import drinkreview.domain.review.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        return "review/write";
    }

    @PostMapping("/review/write")
    public String write(MemberSessionResponseDto memberSessionResponseDto) {
        return "/review/list";
    }

    @GetMapping("/review/read/{id}")
    public String readForm(@PathVariable Long reviewId, HttpServletRequest request, Model model) {
        ReviewResponseDto reviewResponseDto = reviewService.findReview(reviewId);
        List<CommentResponseDto> comments = reviewResponseDto.getComments();

        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        MemberSessionResponseDto memberSessionResponseDto = new MemberSessionResponseDto(member);

        if (memberSessionResponseDto != null) {
            model.addAttribute("memberId", memberSessionResponseDto.getMemberId());

            if (reviewResponseDto.getUserId().equals(memberSessionResponseDto.getId())) {
                model.addAttribute("writer", true);
            }
        }

        reviewService.updateView(reviewId);

        return "review/read";
    }
}
