package drinkreview.domain.order.controller;

import drinkreview.domain.delivery.DeliveryService;
import drinkreview.domain.delivery.dto.DeliveryRequestDto;
import drinkreview.domain.drink.DrinkService;
import drinkreview.domain.drink.dto.DrinkResponseDto;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.order.OrderService;
import drinkreview.domain.orderDrink.Cart;
import drinkreview.domain.orderDrink.CartList;
import drinkreview.global.ui.LoginMember;
import drinkreview.global.ui.SessionConstants;
import drinkreview.global.enums.City;
import drinkreview.global.enums.DrinkStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final DrinkService drinkService;
    private final DeliveryService deliveryService;

    @PostMapping("/drink/buy")
    public String buyDrink(@LoginMember MemberSessionResponseDto loginMember, @Valid OrderForm orderForm, BindingResult result) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        if (result.hasErrors()) {
            return "drink/drink-detail";
        }

        log.info("userId = {}", loginMember.getId());
        log.info("drinkId = {}", orderForm.getDrinkId());
        log.info("count = {}", orderForm.getCount());
        Long orderId = orderService.makeOrder(loginMember.getId(), orderForm.getDrinkId(), orderForm.getCount());

        //make delivery
        test(orderId);

        return "redirect:/shop";
    }

    @PostMapping("/drink/cart")
    public String addCart(@LoginMember MemberSessionResponseDto loginMember, @SessionAttribute(SessionConstants.CART_LIST) CartList cartList,
                          @Valid OrderForm orderForm, BindingResult result) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        if (result.hasErrors()) {
            return "drink/drink-detail";
        }

        if (orderForm.getCount() <= 0) {
            throw new IllegalStateException("Count is must be greater than 0.");
        }

        log.info("userId = {}", loginMember.getId());
        log.info("drinkId = {}", orderForm.getDrinkId());
        log.info("count = {}", orderForm.getCount());

        boolean isDuplicated = false;
        DrinkResponseDto drink = drinkService.findDrinkDto(orderForm.getDrinkId());
        if (drink.getStatus() == DrinkStatus.OUT_OF_STOCK) {
            throw new IllegalStateException("This drink is out of stock.");
        }
        //중복되는 음료수가 존재하는 경우 수량, 가격만 update
        for (int i = 0; i < cartList.getCarts().size(); i++) {
            if (cartList.getCarts().get(i).getDrinkId().equals(drink.getId())) {
                int count = cartList.getCarts().get(i).getCount() + orderForm.getCount();
                int orderPrice = drink.getPrice() * count;
                cartList.getCarts().get(i).update(count, orderPrice);
                isDuplicated = true;
                break;
            }
        }
        //중복되는 음료수가 없을 경우 추가
        if (!isDuplicated) {
            Cart cart = new Cart(drink.getId(), orderForm.getCount(), drink.getPrice() * orderForm.getCount());
            cartList.getCarts().add(cart);
        }

        return "redirect:/shop";
    }

    @GetMapping("/cart/order")
    public String cartOrder(@SessionAttribute(SessionConstants.CART_LIST) CartList cartList, HttpServletRequest request) {
        Map<Long, Integer> orderMap = new HashMap<>();
        List<Cart> carts = cartList.getCarts();

        for (Cart cart : carts) {
            orderMap.put(cart.getDrinkId(), cart.getCount());
        }
        Long orderId = orderService.makeOrderByMap(cartList.getUserId(), orderMap);

        //make delivery
        test(orderId);

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.CART_LIST);
        session.setAttribute(SessionConstants.CART_LIST, new CartList(cartList.getUserId()));

        return "redirect:/shop";
    }

    @GetMapping("/cart/cancel/{id}")
    public String cartCancel(@SessionAttribute(SessionConstants.CART_LIST) CartList cartList, @PathVariable("id") Long drinkId) {
        Cart cart = cartList.getCarts().stream().filter(c -> c.getDrinkId().equals(drinkId)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Cart is null."));
        cartList.getCarts().remove(cart);

        return "redirect:/shop/cart";
    }

    @GetMapping("/order-list/cancel/{id}")
    public String cancel(@LoginMember MemberSessionResponseDto loginMember, @PathVariable("id") Long orderId) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        orderService.cancelOrder(orderId);
        return "redirect:/shop/order-list";
    }

    private void test(Long orderId) {
        DeliveryRequestDto dto = new DeliveryRequestDto();
        dto.setCity(City.SEOUL);
        dto.setStreet("street");
        dto.setEtc("etc");
        deliveryService.makeDelivery(dto, orderId);
    }
}
