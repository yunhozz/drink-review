package drinkreview.domain.comment;

import drinkreview.domain.comment.dto.CommentChildRequestDto;
import drinkreview.domain.comment.dto.CommentRequestDto;
import drinkreview.domain.comment.service.CommentChildService;
import drinkreview.domain.comment.service.CommentService;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.global.ui.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/community/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentChildService commentChildService;

    @PostMapping("/write")
    public String write(@LoginMember MemberSessionResponseDto loginMember, @Valid CommentRequestDto commentRequestDto, BindingResult result,
                        @RequestParam("reviewId") Long reviewId) {
        if (result.hasErrors()) {
            return "community/comment/write";
        }

        commentService.makeComment(commentRequestDto, loginMember.getId(), reviewId);
        return "redirect:/community/review/" + reviewId;
    }

    @GetMapping("/update")
    public String update(@LoginMember MemberSessionResponseDto loginMember, @RequestParam("id") Long commentId, @RequestParam("reviewId") Long reviewId, Model model,
                         HttpServletRequest request) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        model.addAttribute("commentId", commentId);
        model.addAttribute("reviewId", reviewId);

        return "comment/update";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") Long commentId, @RequestParam("reviewId") Long reviewId, @RequestParam String content) {
        commentService.updateComment(commentId, content);
        return "redirect:/community/review/" + reviewId;
    }

    @GetMapping("/delete")
    public String delete(@LoginMember MemberSessionResponseDto loginMember, @RequestParam("id") Long commentId, @RequestParam("reviewId") Long reviewId) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        commentService.deleteComment(commentId, loginMember.getId());
        return "redirect:/community/review/" + reviewId;
    }

    @GetMapping("/reply")
    public String reply(@LoginMember MemberSessionResponseDto loginMember, @RequestParam("reviewId") Long reviewId, @RequestParam("commentId") Long commentId,
                        @ModelAttribute CommentChildRequestDto commentChildRequestDto, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        model.addAttribute("commentId", commentId);
        model.addAttribute("reviewId", reviewId);

        return "comment/reply";
    }

    @PostMapping("/reply")
    public String reply(@LoginMember MemberSessionResponseDto loginMember, @Valid CommentChildRequestDto commentChildRequestDto, BindingResult result,
                        @RequestParam("reviewId") Long reviewId, @RequestParam("commentId") Long commentId) {
        if (result.hasErrors()) {
            return "comment/reply";
        }

        commentChildService.makeCommentChild(commentChildRequestDto, loginMember.getId(), commentId);
        return "redirect:/community/review/" + reviewId;
    }
}
