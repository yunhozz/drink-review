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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "comment_id")
    private List<CommentChild> commentChildList = new ArrayList<>();

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String memberName;

    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted; //Y, N

    private Comment(Member member, String content, String memberName, DeleteStatus isDeleted) {
        this.member = member;
        this.content = content;
        this.memberName = memberName;
        this.isDeleted = isDeleted;
    }

    public static Comment createComment(Member member, Review review, String content) {
        Comment comment = new Comment(member, content, member.getName(), DeleteStatus.N);
        comment.setReview(review);

        return comment;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void addCommentChild(CommentChild commentChild) {
        this.commentChildList.add(commentChild);
    }

    public void deleteMember() {
        if (member != null) {
            member = null;
        }

        memberName = "탈퇴 멤버";
    }

    //연관관계 편의 메소드
    private void setReview(Review review) {
        this.review = review;
        review.getComments().add(this);
    }
}
