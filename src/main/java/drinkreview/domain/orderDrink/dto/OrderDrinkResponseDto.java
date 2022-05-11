package drinkreview.domain.orderDrink.dto;

import drinkreview.domain.orderDrink.OrderDrink;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDrinkResponseDto {

    private Long id;
    private Long orderId;
    private Long drinkId;
    private int orderPrice;
    private int count;

    public OrderDrinkResponseDto(OrderDrink orderDrink) {
        this.id = orderDrink.getId();
        this.orderId = orderDrink.getOrder().getId();
        this.drinkId = orderDrink.getDrink().getId();
        this.orderPrice = orderDrink.getOrderPrice();
        this.count = orderDrink.getCount();
    }
}
