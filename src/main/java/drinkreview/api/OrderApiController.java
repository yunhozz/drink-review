package drinkreview.api;

import drinkreview.domain.order.Order;
import drinkreview.domain.order.dto.OrderQueryDto;
import drinkreview.domain.order.dto.OrderResponseDto;
import drinkreview.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/order")
    public Page<OrderResponseDto> orders(Pageable pageable) {
        Page<Order> page = orderRepository.findPage(pageable);
        return page.map(OrderResponseDto::new);
    }

    @GetMapping("/orders")
    public Page<OrderQueryDto> orderPage(Pageable pageable) {
        return orderRepository.searchPage(pageable);
    }
}
