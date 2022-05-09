package drinkreview.domain.comment;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import drinkreview.global.enums.DeleteStatus;
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
    private String content;

    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted;

    private Comment(Member member, String content, DeleteStatus isDeleted) {
        this.member = member;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public static Comment createComment(Member member, Review review, String content) {
        Comment comment = new Comment(member, content, DeleteStatus.N);
        comment.setReview(review);

        return comment;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    //연관관계 편의 메소드
    private void setReview(Review review) {
        this.review = review;
        review.getComments().add(this);
    }
}
