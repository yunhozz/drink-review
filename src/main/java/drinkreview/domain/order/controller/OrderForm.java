package drinkreview.domain.order.controller;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class OrderForm {

    private Long drinkId;

    @NotNull(message = "Please input count again.")
    @Positive
    private Integer count;
}
