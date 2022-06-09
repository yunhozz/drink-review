package drinkreview.domain.orderDrink;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    private Long drinkId;
    private int count;
    private int orderPrice;

    public Cart(Long drinkId, int count, int orderPrice) {
        this.drinkId = drinkId;
        this.count = count;
        this.orderPrice = orderPrice;
    }

    public void update(int count, int orderPrice) {
        this.count = count;
        this.orderPrice = orderPrice;
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
