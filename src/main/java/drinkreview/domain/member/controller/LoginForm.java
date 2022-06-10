package drinkreview.domain.member.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginForm {

    @NotBlank(message = "Please input ID again.")
    private String memberId;

    @NotBlank(message = "Please input password again.")
    private String memberPw;
}
