package drinkreview.domain.orderDrink;

import drinkreview.domain.drink.Drink;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDrinkRequestDto {

    private Drink drink;
    private int count;

    public OrderDrinkRequestDto(Drink drink, int count) {
        this.drink = drink;
        this.count = count;
    }

    public OrderDrink toEntity() {
        return OrderDrink.builder()
                .drink(drink)
                .orderPrice(drink.getPrice() * count)
                .count(count)
                .build();
    }
}
