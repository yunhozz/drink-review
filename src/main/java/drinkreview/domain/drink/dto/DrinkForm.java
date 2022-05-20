package drinkreview.domain.drink.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DrinkForm {

    @NotBlank private String name;
    @NotBlank private String country;
    private int year;
    private int month;
    private int day;
    private int price;
    private int stockQuantity;
}
