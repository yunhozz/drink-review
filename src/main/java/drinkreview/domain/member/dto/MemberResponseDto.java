package drinkreview.domain.member.dto;

import drinkreview.domain.member.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberResponseDto {

    private String memberId;
    private String memberPw;
    private String name;
    private int age;
    private String auth;

    public MemberResponseDto(Member member) {
        this.memberId = member.getMemberId();
        this.memberPw = member.getMemberPw();
        this.name = member.getName();
        this.age = member.getAge();
        this.auth = member.getAuth();
    }
}
