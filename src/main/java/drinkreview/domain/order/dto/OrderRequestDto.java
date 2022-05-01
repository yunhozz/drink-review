package drinkreview.domain.order.dto;

import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.order.Order;
import drinkreview.domain.orderDrink.OrderDrink;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestDto {

    private Delivery delivery;
    private List<OrderDrink> orderDrinks;

    public OrderRequestDto(Delivery delivery, List<OrderDrink> orderDrinks) {
        this.delivery = delivery;
        this.orderDrinks = orderDrinks;
    }

    public Order toEntity() {
        return Order.createOrder(delivery, orderDrinks);
    }
}
