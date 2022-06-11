package drinkreview.domain.order.dto;

import drinkreview.domain.order.OrderHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderHistoryResponseDto {

    private Long id;
    private Long userId;
    private String memberId;
    private String name;
    private List<OrderEntityResponseDto> orderEntities;

    public OrderHistoryResponseDto(OrderHistory orderHistory) {
        id = orderHistory.getId();
        userId = orderHistory.getMemberInfo().getUserId();
        memberId = orderHistory.getMemberInfo().getMemberId();
        name = orderHistory.getMemberInfo().getName();
        orderEntities = orderHistory.getOrderEntities().stream()
                .map(OrderEntityResponseDto::new)
                .toList();
    }
}
