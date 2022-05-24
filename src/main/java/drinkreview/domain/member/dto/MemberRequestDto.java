package drinkreview.domain.member.dto;

import drinkreview.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberRequestDto {

    @NotBlank private String memberId;
    @NotBlank private String memberPw;
    @NotBlank private String name;
    @NotNull private Integer age;
    private String auth;

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
}
