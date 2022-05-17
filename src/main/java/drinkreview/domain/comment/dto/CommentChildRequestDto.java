package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.comment.CommentChild;
import drinkreview.domain.member.Member;
import drinkreview.global.enums.DeleteStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentChildRequestDto {

    private Member member;
    private Comment comment;
    private String content;

    public CommentChild toEntity() {
        return new CommentChild(member, content, member.getName(), DeleteStatus.N);
    }
}
