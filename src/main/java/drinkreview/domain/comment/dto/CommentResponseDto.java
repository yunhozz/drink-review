package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long userId;
    private Long reviewId;
    private String comments;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getMember().getId();
        this.reviewId = comment.getReview().getId();
        this.comments = comment.getComments();
    }
}
