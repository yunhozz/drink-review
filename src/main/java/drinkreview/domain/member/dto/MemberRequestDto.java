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

    @NotBlank(message = "Please input ID again.")
    private String memberId;

    @NotBlank(message = "Please input password again.")
    private String memberPw;

    @NotBlank(message = "Please input name again.")
    private String name;

    @NotNull(message = "Please input age again.")
    private Integer age;

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
