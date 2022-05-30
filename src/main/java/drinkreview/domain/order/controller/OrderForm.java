package drinkreview.domain.order.controller;

import lombok.Data;

@Data
public class OrderForm {

    private Long drinkId;
    private int count;
}
