package drinkreview.domain.comment.dto;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CommentRequestDto {

    private Member member;
    private Review review;
    private String content;
    private Comment parent;
    private List<Comment> childList;

    public Comment toEntity() {
        return Comment.createComment(member, review, content, parent, childList);
    }
}
