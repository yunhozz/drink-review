package drinkreview.domain.order;

import drinkreview.domain.member.Member;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.domain.order.dto.OrderRequestDto;
import drinkreview.domain.order.dto.OrderResponseDto;
import drinkreview.domain.order.repository.OrderHistoryRepository;
import drinkreview.domain.order.repository.OrderRepository;
import drinkreview.domain.orderDrink.OrderDrink;
import drinkreview.domain.orderDrink.OrderDrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDrinkRepository orderDrinkRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final MemberRepository memberRepository;

    public Long makeOrder(Long userId, List<Long> orderDrinkIds) {
        List<OrderDrink> orderDrinks = new ArrayList<>();
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));

        for (Long orderDrinkId : orderDrinkIds) {
            OrderDrink orderDrink = orderDrinkRepository.findById(orderDrinkId)
                    .orElseThrow(() -> new IllegalStateException("This orderDrink is null. " + orderDrinkId));

            orderDrinks.add(orderDrink);
        }

        OrderRequestDto dto = new OrderRequestDto();
        dto.setMember(member);
        dto.setOrderDrinks(orderDrinks);
        Order order = orderRepository.save(dto.toEntity());

        Optional<OrderHistory> findHistory = orderHistoryRepository.findByUserId(member.getId());
        OrderEntity orderEntity = new OrderEntity(order.getId());

        if (findHistory.isEmpty()) {
            OrderHistory orderHistory = new OrderHistory(member, orderEntity);
            orderHistoryRepository.save(orderHistory); //cascade : orderEntity persist
        } else {
            OrderHistory orderHistory = findHistory.get();
            orderHistory.update(orderEntity);
            orderHistoryRepository.save(orderHistory); //cascade : orderEntity persist
        }

        return order.getId();
    }

    public void updateOrder(Long orderId, Long orderDrinkId, int count) {
        Order order = this.findOrder(orderId);
        OrderDrink orderDrink = orderDrinkRepository.findById(orderDrinkId)
                .orElseThrow(() -> new IllegalStateException("OrderDrink is null."));

        order.updateCountOfDrink(orderDrink, count);
    }

    public void cancelOrder(Long orderId) {
        Order order = this.findOrder(orderId);
        order.cancel();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findOrderDto(Long orderId) {
        Order order = this.findOrder(orderId);
        return new OrderResponseDto(order);
    }

    @Transactional(readOnly = true)
    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order is null."));
    }
}
