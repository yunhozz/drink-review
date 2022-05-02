package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    private Member member;
    private Review review;
    private String comments;

    @Builder
    private CommentRequestDto(Member member, Review review, String comments) {
        this.member = member;
        this.review = review;
        this.comments = comments;
    }

    public Comment toEntity() {
        return Comment.createComment(member, review, comments);
    }
}
