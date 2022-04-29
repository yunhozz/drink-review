package drinkreview.domain.orderDrink;

import drinkreview.domain.drink.Drink;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDrinkRequestDto {

    private Drink drink;
    private int count;

    public OrderDrink toEntity() {
        return OrderDrink.builder()
                .drink(drink)
                .orderPrice(drink.getPrice() * count)
                .count(count)
                .build();
    }
}
