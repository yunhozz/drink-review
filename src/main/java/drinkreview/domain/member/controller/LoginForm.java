package drinkreview.domain.member.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class LoginForm {

    @NotEmpty private String memberId;
    @NotEmpty private String memberPw;
}
