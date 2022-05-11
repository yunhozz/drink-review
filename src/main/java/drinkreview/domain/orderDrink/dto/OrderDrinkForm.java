package drinkreview.domain.orderDrink.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderDrinkForm {

    @NotEmpty private Long drinkId;
    @NotEmpty private int count;
}
