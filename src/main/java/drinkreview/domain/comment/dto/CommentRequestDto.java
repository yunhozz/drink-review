package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentRequestDto {

    @NotNull private Member member;
    @NotNull private Review review;
    @NotBlank private String content;

    public Comment toEntity() {
        return Comment.createComment(member, review, content);
    }
}
