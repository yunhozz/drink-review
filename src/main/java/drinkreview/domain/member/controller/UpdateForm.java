package drinkreview.domain.member.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateForm {

    @NotNull private Long id;
    @NotBlank private String originPw;
    @NotBlank private String newPw;
    @NotBlank private String name;
    @NotNull private Integer age;
}
