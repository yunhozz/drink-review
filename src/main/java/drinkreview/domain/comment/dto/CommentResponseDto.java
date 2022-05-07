package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long userId;
    private Long reviewId;
    private String content;
    private Long parentId;
    private List<ChildResponseDto> child;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getMember().getId();
        this.reviewId = comment.getReview().getId();
        this.content = comment.getContent();
        this.parentId = comment.getParent().getId();
        this.child = comment.getChild().stream()
                .map(ChildResponseDto::new)
                .toList();
    }
}
