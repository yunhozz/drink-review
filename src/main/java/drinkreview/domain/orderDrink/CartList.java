package drinkreview.domain.orderDrink;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CartList {

    private Long userId;
    private List<Cart> carts = new ArrayList<>();

    public CartList(Long userId, List<Cart> carts) {
        this.userId = userId;
        this.carts = carts;
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
