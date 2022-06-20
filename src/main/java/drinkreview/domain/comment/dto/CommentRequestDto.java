package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentRequestDto {

    private Member member;
    private Review review;

    @NotBlank(message = "Please input content.")
    private String content;

    public Comment toEntity() {
        return Comment.createComment(member, review, content);
    }
}
