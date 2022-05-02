package drinkreview.domain.comment;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comments;

    private Comment(Member member, String comments) {
        this.member = member;
        this.comments = comments;
    }

    public static Comment createComment(Member member, Review review, String comments) {
        Comment comment = new Comment(member, comments);
        comment.setReview(review);

        return comment;
    }

    public void updateComments(String comments) {
        this.comments = comments;
    }

    /**
     * 연관관계 편의 메소드
     */
    private void setReview(Review review) {
        this.review = review;
        review.getComments().add(this);
    }
}
