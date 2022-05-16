package drinkreview.api;

import drinkreview.domain.comment.dto.CommentQueryDto;
import drinkreview.domain.comment.dto.CommentResponseDto;
import drinkreview.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentRepository commentRepository;

    @GetMapping("/review/{id}/comment")
    public List<CommentResponseDto> commentList(@PathVariable Long reviewId) {
        return commentRepository.searchListTest(reviewId);
    }

    @GetMapping("/review/{id}/comments")
    public List<CommentQueryDto> commentPage(@PathVariable Long reviewId) {
        return commentRepository.searchList(reviewId);
    }
}
