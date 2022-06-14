package drinkreview.domain.order;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.repository.DrinkRepository;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.domain.order.dto.OrderHistoryResponseDto;
import drinkreview.domain.order.dto.OrderResponseDto;
import drinkreview.domain.order.repository.OrderHistoryRepository;
import drinkreview.domain.order.repository.OrderRepository;
import drinkreview.domain.orderDrink.OrderDrink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final MemberRepository memberRepository;
    private final DrinkRepository drinkRepository;

    public Long makeOrder(Long userId, Long drinkId, int count) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));

        OrderDrink orderDrink = OrderDrink.createOrderDrink(drink, count);
        Order order = Order.createOrder(member, List.of(orderDrink));
        orderRepository.save(order); //cascade persist : OrderDrink

        Optional<OrderHistory> findHistory = orderHistoryRepository.findWithUserId(member.getId());
        OrderEntity orderEntity = new OrderEntity(order.getId());
        OrderHistory orderHistory;

        if (findHistory.isEmpty()) {
            orderHistory = OrderHistory.createOrderHistory(member, orderEntity);
        } else {
            orderHistory = findHistory.get();
            orderHistory.updateOrderEntity(orderEntity);
        }
        orderHistoryRepository.save(orderHistory); //cascade : orderEntity persist

        return order.getId();
    }

    public Long makeOrderByMap(Long userId, Map<Long, Integer> orderMap) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));
        List<OrderDrink> orderDrinks = new ArrayList<>();

        for (Long drinkId : orderMap.keySet()) {
            Drink drink = drinkRepository.findById(drinkId)
                    .orElseThrow(() -> new IllegalStateException("Drink is null : " + drinkId));
            OrderDrink orderDrink = OrderDrink.createOrderDrink(drink, orderMap.get(drinkId));
            orderDrinks.add(orderDrink);
        }
        Order order = Order.createOrder(member, orderDrinks);
        orderRepository.save(order); //cascade persist : OrderDrink

        Optional<OrderHistory> findHistory = orderHistoryRepository.findWithUserId(member.getId());
        OrderEntity orderEntity = new OrderEntity(order.getId());
        OrderHistory orderHistory;

        if (findHistory.isEmpty()) {
            orderHistory = OrderHistory.createOrderHistory(member, orderEntity);
        } else {
            orderHistory = findHistory.get();
            orderHistory.updateOrderEntity(orderEntity);
        }
        orderHistoryRepository.save(orderHistory); //cascade : orderEntity persist

        return order.getId();
    }

    public void cancelOrder(Long orderId) {
        Order order = this.findOrder(orderId);
        order.cancel();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findOrderDto(Long orderId) {
        Order order = orderRepository.findWithOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("Order is null."));
        return new OrderResponseDto(order);
    }

    @Transactional(readOnly = true)
    public OrderHistoryResponseDto findOrderHistoryDto(Long userId) {
        OrderHistory orderHistory = orderHistoryRepository.findWithUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Order history is null with userId : " + userId));
        return new OrderHistoryResponseDto(orderHistory);
    }

    @Transactional(readOnly = true)
    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order is null."));
    }
}
