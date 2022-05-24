package drinkreview.domain.drink.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DrinkForm {

    @NotBlank private String name;
    @NotBlank private String country;
    @NotNull private Integer year;
    @NotNull private Integer month;
    @NotNull private Integer day;
    @NotNull private Integer price;
    @NotNull private Integer stockQuantity;
}
