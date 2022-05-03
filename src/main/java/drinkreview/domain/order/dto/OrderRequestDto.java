package drinkreview.domain.order.dto;

import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.order.Order;
import drinkreview.domain.orderDrink.OrderDrink;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderRequestDto {

    private Delivery delivery;
    private List<OrderDrink> orderDrinks;

    public Order toEntity() {
        return Order.createOrder(delivery, orderDrinks);
    }
}
