package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.global.enums.DeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long userId;
    private Long reviewId;
    private String content;
    private String memberName;
    private DeleteStatus isDeleted;
    private List<CommentChildResponseDto> commentChildList;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getMember().getId();
        this.reviewId = comment.getReview().getId();
        this.content = comment.getContent();
        this.memberName = comment.getMemberName();
        this.isDeleted = comment.getIsDeleted();
        this.commentChildList = comment.getCommentChildList().stream()
                .map(CommentChildResponseDto::new)
                .toList();
    }
}
