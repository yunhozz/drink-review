package drinkreview.global.controller;

import drinkreview.domain.drink.dto.DrinkQueryDto;
import drinkreview.domain.drink.repository.DrinkRepository;
import drinkreview.domain.orderDrink.Cart;
import drinkreview.domain.orderDrink.CartList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final DrinkRepository drinkRepository;

    @GetMapping
    public String shop(@SessionAttribute(SessionConstant.CART_LIST) CartList cartList, @PageableDefault(size = 8) Pageable pageable, Model model) {
        Page<DrinkQueryDto> drinks = drinkRepository.searchSimplePageDrink(pageable);
        model.addAttribute("drinks", drinks);
        model.addAttribute("carts", cartList.getCarts());

        return "shop";
    }

    @GetMapping("/cart")
    public String cart(@SessionAttribute(SessionConstant.CART_LIST) CartList cartList, Model model) {
        List<Cart> carts = cartList.getCarts();
        List<DrinkQueryDto> drinks = new ArrayList<>();
        int totalPrice = 0;

        for (Cart cart : carts) {
            DrinkQueryDto drink = drinkRepository.searchDrinkOnCart(cart.getDrinkId());
            drinks.add(drink);
            totalPrice += cart.getOrderPrice();
        }
        model.addAttribute("drinks", drinks);
        model.addAttribute("carts", carts);
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

    @GetMapping("/cart/cancel/{id}")
    public String cartCancel(@SessionAttribute(SessionConstant.CART_LIST) CartList cartList, @PathVariable("id") Long drinkId) {
        return null;
    }

    @GetMapping("/cart/order")
    public String cartOrder() {
        return null;
    }
}
