package drinkreview.domain.member.dto;

import drinkreview.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class MemberSessionResponseDto implements Serializable {

    private Long id;
    private String memberId;
    private String memberPw;
    private String name;
    private int age;
    private String auth;

    public MemberSessionResponseDto(Member member) {
        id = member.getId();
        memberId = member.getMemberId();
        memberPw = member.getMemberPw();
        name = member.getName();
        age = member.getAge();
        auth = member.getAuth();
    }
}
