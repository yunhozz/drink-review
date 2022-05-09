package drinkreview.domain.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import drinkreview.domain.orderDrink.dto.OrderDrinkResponseDto;
import drinkreview.global.enums.DeliveryStatus;
import drinkreview.global.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderQueryDto {

    //Order
    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;

    //Member
    private Long userId;
    private String name;

    //Delivery
    private Long deliveryId;
    private DeliveryStatus deliveryStatus;

    //OrderDrink
    private List<OrderDrinkResponseDto> orderDrinks;

    @QueryProjection
    public OrderQueryDto(Long id, LocalDateTime orderDate, OrderStatus orderStatus, Long userId, String name, Long deliveryId, DeliveryStatus deliveryStatus) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.name = name;
        this.deliveryId = deliveryId;
        this.deliveryStatus = deliveryStatus;
    }
}
