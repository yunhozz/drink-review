package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDto {

    private Member member;
    private Review review;
    private String comments;

    public Comment toEntity() {
        return Comment.createComment(member, review, comments);
    }
}
