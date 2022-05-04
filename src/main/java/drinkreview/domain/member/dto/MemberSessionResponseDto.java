package drinkreview.domain.member.dto;

import drinkreview.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class MemberSessionResponseDto implements Serializable {

    private Long id;
    private String memberId;
    private String memberPw;
    private String name;
    private int age;
    private String auth;

    public MemberSessionResponseDto(Member member) {
        this.id = member.getId();
        this.memberId = member.getMemberId();
        this.memberPw = member.getMemberPw();
        this.name = member.getName();
        this.age = member.getAge();
        this.auth = member.getAuth();
    }
}
