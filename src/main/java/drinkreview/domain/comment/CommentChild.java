package drinkreview.domain.comment;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentChild extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(length = 1000)
    private String content;

    private String memberName;

    public CommentChild(Member member, Comment comment, String content, String memberName) {
        this.member = member;
        this.comment = comment;
        this.content = content;
        this.memberName = memberName;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deleteMember() {
        if (member != null) {
            member = null;
        }
        memberName = "<withdrawal member>";
    }
}
