package drinkreview.domain.order.controller;

import drinkreview.domain.drink.DrinkService;
import drinkreview.domain.drink.dto.DrinkResponseDto;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.order.OrderService;
import drinkreview.domain.orderDrink.Cart;
import drinkreview.domain.orderDrink.CartList;
import drinkreview.global.controller.SessionConstant;
import drinkreview.global.enums.DrinkStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final DrinkService drinkService;

    @PostMapping("/drink/buy")
    public String buyDrink(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @Valid OrderForm orderForm, BindingResult result) {
        if (loginMember == null) {
            return "member/login";
        }

        if (result.hasErrors()) {
            return "drink/drink-detail";
        }

        log.info("userId = {}", loginMember.getId());
        log.info("drinkId = {}", orderForm.getDrinkId());
        log.info("count = {}", orderForm.getCount());
        orderService.makeOrder(loginMember.getId(), orderForm.getDrinkId(), orderForm.getCount());

        return "redirect:/shop";
    }

    @PostMapping("/drink/cart")
    public String addCart(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember,
                          @SessionAttribute(SessionConstant.CART_LIST) CartList cartList, @Valid OrderForm orderForm, BindingResult result) {
        if (loginMember == null) {
            return "member/login";
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

    @GetMapping("/order-list/cancel")
    public String cancel() {
        return null;
    }
}
