package drinkreview.domain.orderDrink;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Cart {

    private Long drinkId;
    private int count;
    private int orderPrice;

    public Cart(OrderDrinkResponseDto orderDrink) {
        drinkId = orderDrink.getDrinkId();
        count = orderDrink.getCount();
        orderPrice = orderDrink.getOrderPrice();
    }

    @Getter
    @AllArgsConstructor
    public static class CartResponseDto {
        private Long drinkId;
        private int count;
        private int orderPrice;

        public CartResponseDto(Cart cart) {
            drinkId = cart.getDrinkId();
            count = cart.getCount();
            orderPrice = cart.getOrderPrice();
        }
    }
}
