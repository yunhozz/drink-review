package drinkreview.domain.orderDrink.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrderDrinkForm {

    @NotBlank private Long drinkId;
    private int count;
}
