package drinkreview.api;

import drinkreview.domain.comment.CommentService;
import drinkreview.domain.comment.dto.CommentQueryDto;
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
    private final CommentService commentService;

    @GetMapping("/review/{id}/list")
    public List<CommentQueryDto> commentList(@PathVariable Long reviewId) {
        return commentRepository.searchList();
    }
}
