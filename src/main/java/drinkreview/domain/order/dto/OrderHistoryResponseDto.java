package drinkreview.domain.order.dto;

import drinkreview.domain.order.history.OrderHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderHistoryResponseDto {

    private Long id;
    private Long userId;
    private List<OrderEntityResponseDto> orderEntities;

    public OrderHistoryResponseDto(OrderHistory orderHistory) {
        id = orderHistory.getId();
        userId = orderHistory.getMemberInfo().getUserId();
        orderEntities = orderHistory.getOrderEntities().stream()
                .map(OrderEntityResponseDto::new)
                .toList();
    }
}
