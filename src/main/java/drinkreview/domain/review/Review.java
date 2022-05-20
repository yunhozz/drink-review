package drinkreview.domain.review;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.comment.Comment;
import drinkreview.domain.drink.Drink;
import drinkreview.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drink_id")
    private Drink drink;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    private String title;

    @Column(length = 2000)
    private String content;

    private String memberName;
    private double score;
    private int view;

    @Builder
    private Review(Member member, Drink drink, String title, String content, String memberName, double score, int view) {
        this.member = member;
        this.drink = drink;
        this.title = title;
        this.content = content;
        this.memberName = memberName;
        this.score = score;
        this.view = view;
    }

    public void updateField(String title, String content, double score) {
        this.title = title;
        this.content = content;
        this.score = score;
    }

    public void deleteMember() {
        if (member != null) {
            member = null;
        }
        memberName = "탈퇴 멤버";
    }
}
