package drinkreview.domain.comment;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.member.Member;
import drinkreview.global.enums.DeleteStatus;
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

    private String content;
    private String memberName;

    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted; //Y, N

    public CommentChild(Member member, String content, String memberName, DeleteStatus isDeleted) {
        this.member = member;
        this.content = content;
        this.memberName = memberName;
        this.isDeleted = isDeleted;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deleteMember() {
        if (member != null) {
            member = null;
        }

        memberName = "탈퇴 멤버";
    }
}
