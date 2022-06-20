package drinkreview.domain.member.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class UpdateForm {

    @NotNull
    private Long id;

    @NotBlank(message = "Please input original password.")
    private String originPw;

    @NotBlank(message = "Please input new password.")
    private String newPw;

    @NotBlank(message = "Please input name.")
    private String name;

    @NotNull(message = "Please choose age.")
    @Positive
    private Integer age;
}
