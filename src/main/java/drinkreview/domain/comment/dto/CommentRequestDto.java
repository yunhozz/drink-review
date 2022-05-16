package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    private Member member;
    private Review review;
    private String content;

    public Comment toEntity() {
        return Comment.createComment(member, review, content);
    }
}
