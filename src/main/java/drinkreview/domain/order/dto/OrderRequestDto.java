package drinkreview.domain.order.dto;

import drinkreview.domain.member.Member;
import drinkreview.domain.order.Order;
import drinkreview.domain.orderDrink.OrderDrink;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderRequestDto {

    private Member member;
    private List<OrderDrink> orderDrinks;

    public Order toEntity() {
        return Order.createOrder(member, orderDrinks);
    }
}
