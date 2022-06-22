package drinkreview.domain.member.dto;

import drinkreview.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class MemberRequestDto {

    @NotBlank(message = "You must enter an ID.")
    private String memberId;

    @NotBlank(message = "You must enter an password.")
    private String memberPw;

    @NotBlank(message = "You must enter your name.")
    private String name;

    @NotNull(message = "You must enter your age.")
    @Positive(message = "You must select positive number of age.")
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
