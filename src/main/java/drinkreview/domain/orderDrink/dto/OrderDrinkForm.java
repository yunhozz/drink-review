package drinkreview.domain.orderDrink.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class OrderDrinkForm {

    @NotBlank private Long drinkId;
    @NotEmpty private int count;
}
