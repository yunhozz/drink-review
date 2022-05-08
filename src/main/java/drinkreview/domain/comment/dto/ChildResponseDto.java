package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChildResponseDto {

    private Long id;
    private Long userId;
    private Long parentId;
    private String content;

    public ChildResponseDto(Comment child) {
        this.id = child.getId();
        this.userId = child.getMember().getId();
        this.parentId = child.getParent().getId();
        this.content = child.getContent();
    }
}
