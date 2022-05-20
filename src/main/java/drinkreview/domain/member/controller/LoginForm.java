package drinkreview.domain.member.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginForm {

    @NotBlank private String memberId;
    @NotBlank private String memberPw;
}
