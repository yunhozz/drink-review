package drinkreview.domain.member.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
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
    @Positive(message = "You must select positive number of age.")
    private Integer age;
}
