package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.comment.CommentChild;
import drinkreview.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentChildRequestDto {

    private Member member;
    private Comment comment;

    @NotBlank(message = "Please input content.")
    private String content;

    public CommentChild toEntity() {
        return new CommentChild(member, comment, content, member.getName());
    }
}
