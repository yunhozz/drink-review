package drinkreview.domain.member.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginForm {

    @NotBlank(message = "You must enter an ID.")
    private String memberId;

    @NotBlank(message = "You must enter an password.")
    private String memberPw;
}
