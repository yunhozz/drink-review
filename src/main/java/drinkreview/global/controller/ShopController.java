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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;
    private final OrderDrinkService orderDrinkService;
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

    @GetMapping("/order-list")
    public String orderList(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, Model model) {
        if (loginMember == null) {
            return "member/login";
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

    @GetMapping("/cart/cancel/{id}")
    public String cartCancel(@SessionAttribute(SessionConstant.CART_LIST) CartList cartList, @PathVariable("id") Long drinkId) {
        Cart cart = cartList.getCarts().stream().filter(c -> c.getDrinkId().equals(drinkId)).findFirst()
                        .orElseThrow(() -> new IllegalStateException("Cart is null."));
        cartList.getCarts().remove(cart);

        return "redirect:/shop";
    }

    @GetMapping("/cart/order")
    public String cartOrder(@SessionAttribute(SessionConstant.CART_LIST) CartList cartList, HttpServletRequest request) {
        Map<Long, Integer> orderMap = new HashMap<>();
        List<Cart> carts = cartList.getCarts();

        for (Cart cart : carts) {
            orderMap.put(cart.getDrinkId(), cart.getCount());
        }
        orderService.makeOrderByMap(cartList.getUserId(), orderMap);

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstant.CART_LIST);
        session.setAttribute(SessionConstant.CART_LIST, new CartList(cartList.getUserId()));

        return "redirect:/shop";
    }
}
