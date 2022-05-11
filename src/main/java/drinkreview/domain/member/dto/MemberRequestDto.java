package drinkreview.domain.member.dto;

import drinkreview.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberRequestDto {

    @NotEmpty private String memberId;
    @NotEmpty private String memberPw;
    @NotEmpty private String name;
    @NotEmpty private int age;
    @NotEmpty private String auth;

    public Member toEntity() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPw = encoder.encode(memberPw);

        return Member.builder()
                .memberId(memberId)
                .memberPw(encodedPw)
                .name(name)
                .age(age)
                .auth(auth)
                .build();
    }

    public Member toEntityWithoutEncoder() {
        return Member.builder()
                .memberId(memberId)
                .memberPw(memberPw)
                .name(name)
                .age(age)
                .auth(auth)
                .build();
    }
}
