package drinkreview.global.controller;

import drinkreview.domain.drink.dto.DrinkQueryDto;
import drinkreview.domain.drink.repository.DrinkRepository;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.order.OrderService;
import drinkreview.domain.order.dto.OrderHistoryResponseDto;
import drinkreview.domain.order.dto.OrderResponseDto;
import drinkreview.domain.orderDrink.Cart;
import drinkreview.domain.orderDrink.CartList;
import drinkreview.domain.orderDrink.OrderDrinkResponseDto;
import drinkreview.domain.orderDrink.OrderDrinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;
    private final OrderDrinkService orderDrinkService;
    private final DrinkRepository drinkRepository;

    @GetMapping
    public String shop(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember,
                       @SessionAttribute(SessionConstant.CART_LIST) CartList cartList, @PageableDefault(size = 8) Pageable pageable, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

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

    @GetMapping("/order-list")
    public String orderList(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        List<OrderResponseDto> orders = new ArrayList<>();
        OrderHistoryResponseDto orderHistory = orderService.findOrderHistoryDto(loginMember.getId());

        for (int i = 0; i < orderHistory.getOrderEntities().size(); i++) {
            Long orderId = orderHistory.getOrderEntities().get(i).getOrderId();
            OrderResponseDto order = orderService.findOrderDto(orderId);
            orders.add(order);
        }
        model.addAttribute("orderHistory", orderHistory);
        model.addAttribute("orders", orders);

        List<OrderDrinkResponseDto> orderDrinkList = orderDrinkService.findOrderDrinkDtoList();
        model.addAttribute("orderDrinkList", orderDrinkList);

        return "order/order-list";
    }
}
