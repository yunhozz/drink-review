package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.global.enums.DeleteStatus;
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
    private DeleteStatus isDeleted;
    private List<ChildResponseDto> childList;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getMember().getId();
        this.reviewId = comment.getReview().getId();
        this.content = comment.getContent();
        this.parentId = comment.getParent().getId();
        this.isDeleted = comment.getIsDeleted();
        this.childList = comment.getChildList().stream()
                .map(ChildResponseDto::new)
                .toList();
    }
}
