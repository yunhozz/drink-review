package drinkreview.domain.orderDrink.dto;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.orderDrink.OrderDrink;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDrinkRequestDto {

    private Drink drink;
    private int count;

    public OrderDrink toEntity() {
        return OrderDrink.createOrderDrink(drink, count);
    }
}
