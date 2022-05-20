package drinkreview.domain.order.dto;

import drinkreview.domain.member.Member;
import drinkreview.domain.order.Order;
import drinkreview.domain.orderDrink.OrderDrink;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    @NotNull private Member member;
    @NotEmpty private List<OrderDrink> orderDrinks;

    public Order toEntity() {
        return Order.createOrder(member, orderDrinks);
    }
}
