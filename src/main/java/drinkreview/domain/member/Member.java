package drinkreview.domain.member;

import drinkreview.domain.TimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String memberId;

    private String memberPw;
    private String name;
    private int age;
    private String auth;

    @Builder
    private Member(String memberId, String memberPw, String name, int age, String auth) {
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.name = name;
        this.age = age;
        this.auth = auth;
    }
}
