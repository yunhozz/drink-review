package drinkreview.domain.comment;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> child = new ArrayList<>();

    public Comment(Member member, String content, Comment parent) {
        this.member = member;
        this.content = content;
        this.parent = parent;
    }

    public static Comment createComment(Member member, Review review, String content, Comment parent, List<Comment> childList) {
        Comment comment = new Comment(member, content, parent);
        comment.setReview(review);

        for (Comment child : childList) {
            comment.setChild(child);
        }

        return comment;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    /**
     * 연관관계 편의 메소드
     */
    private void setReview(Review review) {
        this.review = review;
        review.getComments().add(this);
    }

    private void setChild(Comment child) {
        this.child.add(child);
    }
}
