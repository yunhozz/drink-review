package drinkreview.domain.drink.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class DrinkForm {

    @NotBlank private String name;
    @NotBlank private String country;
    @NotEmpty private int year;
    @NotEmpty private int month;
    @NotEmpty private int day;
    @NotEmpty private int price;
    @NotEmpty private int stockQuantity;
}
