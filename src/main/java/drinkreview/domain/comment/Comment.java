package drinkreview.domain.comment;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import drinkreview.global.enums.DeleteStatus;
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
    private Comment parent; //상위 댓글

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> childList = new ArrayList<>(); //하위 댓글

    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted;

    private Comment(Member member, String content, DeleteStatus isDeleted) {
        this.member = member;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public static Comment createComment(Member member, Review review, String content, Comment parent) {
        Comment comment = new Comment(member, content, DeleteStatus.N);
        comment.setReview(review);

        if (parent != null) {
            comment.setParent(parent);
        }

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

    private void setParent(Comment parent) {
        this.parent = parent;
    }
}
