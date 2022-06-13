package drinkreview.domain.order.dto;

import drinkreview.domain.order.Order;
import drinkreview.domain.orderDrink.OrderDrinkResponseDto;
import drinkreview.global.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private Long userId;
    private Long deliveryId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderDrinkResponseDto> orderDrinks;

    public OrderResponseDto(Order order) {
        id = order.getId();
        userId = order.getMember().getId();
        deliveryId = order.getDelivery().getId();
        orderDate = order.getOrderDate();
        status = order.getStatus();
        orderDrinks = order.getOrderDrinks().stream()
                .map(OrderDrinkResponseDto::new)
                .toList();
    }
}
