package drinkreview.domain.order.controller;

import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.order.OrderService;
import drinkreview.global.controller.LoginSessionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/drink/buy")
    public String buyDrink(@SessionAttribute(LoginSessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember,
                           @Valid OrderForm orderForm) {
        if (loginMember == null) {
            return "member/login";
        }

        log.info("drinkId = {}", orderForm.getDrinkId());
        log.info("count = {}", orderForm.getCount());
        orderService.makeOrder(loginMember.getId(), orderForm.getDrinkId(), orderForm.getCount());

        return "redirect:/shop";
    }
}
