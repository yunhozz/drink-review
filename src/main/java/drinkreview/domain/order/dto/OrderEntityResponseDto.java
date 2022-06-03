package drinkreview.domain.order.dto;

import drinkreview.domain.order.history.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderEntityResponseDto {

    private Long id;
    private Long orderId;

    public OrderEntityResponseDto(OrderEntity orderEntity) {
        id = orderEntity.getId();
        orderId = orderEntity.getOrderId();
    }
}
