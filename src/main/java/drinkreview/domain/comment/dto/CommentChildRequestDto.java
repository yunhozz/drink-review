package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.comment.CommentChild;
import drinkreview.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentChildRequestDto {

    @NotNull private Member member;
    @NotNull private Comment comment;
    @NotBlank private String content;

    public CommentChild toEntity() {
        return new CommentChild(member, content, member.getName());
    }
}
