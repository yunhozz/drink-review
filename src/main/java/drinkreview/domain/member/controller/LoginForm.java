package drinkreview.domain.member.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginForm {

    @NotBlank(message = "You must enter an ID.")
    private String memberId;

    @NotBlank(message = "You must enter an password.")
    private String memberPw;
}
