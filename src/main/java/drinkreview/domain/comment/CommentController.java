package drinkreview.domain.comment;

import drinkreview.domain.comment.dto.CommentRequestDto;
import drinkreview.domain.member.UserDetailsImpl;
import drinkreview.domain.review.ReviewService;
import drinkreview.domain.review.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ReviewService reviewService;

    @GetMapping("/review/{id}/comment")
    public String commentForm(@PathVariable Long reviewId, Model model) {
        ReviewResponseDto reviewDto = reviewService.findReviewDto(reviewId);
        model.addAttribute("reviewId", reviewDto.getId());
        model.addAttribute("commentDto", new CommentRequestDto());

        return "comment/write";
    }

    @PostMapping("/review/{id}/comment")
    public String saveComment(@PathVariable Long reviewId, @Valid CommentRequestDto dto, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "review/{id}/comment";
        }

        ReviewResponseDto reviewDto = reviewService.findReviewDto(reviewId);
        HttpSession session = request.getSession();
        UserDetailsImpl memberDetails = (UserDetailsImpl) session.getAttribute("member");

        if (memberDetails != null) {
            commentService.makeComment(dto, memberDetails.getMember().getId(), reviewDto.getId());
        }

        return "redirect:/review/list";
    }

    @GetMapping("/review/{id}/comments")
    public String commentList(@PathVariable Long reviewId) {
        ReviewResponseDto reviewDto = reviewService.findReviewDto(reviewId);


        return "comment/commentList";
    }
}
