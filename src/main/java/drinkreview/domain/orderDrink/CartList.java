package drinkreview.domain.orderDrink;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartList {

    private Long userId;
    private List<Cart> carts = new ArrayList<>();

    public CartList(Long userId) {
        this.userId = userId;
    }

    @Getter
    @AllArgsConstructor
    public static class CartListResponseDto {
        private Long userId;
        private List<Cart> carts;

        public CartListResponseDto(CartList cartList) {
            userId = cartList.getUserId();
            carts = cartList.getCarts();
        }
    }
}
