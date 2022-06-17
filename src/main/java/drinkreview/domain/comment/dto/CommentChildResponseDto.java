package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.CommentChild;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentChildResponseDto {

    private Long id;
    private Long userId;
    private Long commentId;
    private String content;
    private String memberName;

    public CommentChildResponseDto(CommentChild commentChild) {
        id = commentChild.getId();
        userId = commentChild.getMember().getId();
        commentId = commentChild.getComment().getId();
        content = commentChild.getContent();
        memberName = commentChild.getMemberName();
    }
}
