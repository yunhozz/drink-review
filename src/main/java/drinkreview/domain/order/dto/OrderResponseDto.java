package drinkreview.domain.order.dto;

import drinkreview.domain.order.Order;
import drinkreview.domain.orderDrink.dto.OrderDrinkResponseDto;
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
        this.id = order.getId();
        this.userId = order.getMember().getId();
        this.deliveryId = order.getDelivery().getId();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.orderDrinks = order.getOrderDrinks().stream()
                .map(OrderDrinkResponseDto::new)
                .toList();
    }
}
