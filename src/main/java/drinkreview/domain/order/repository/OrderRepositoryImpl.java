package drinkreview.domain.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import drinkreview.domain.order.dto.OrderQueryDto;
import drinkreview.domain.order.dto.QOrderQueryDto;
import drinkreview.domain.orderDrink.dto.OrderDrinkResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static drinkreview.domain.delivery.QDelivery.delivery;
import static drinkreview.domain.drink.QDrink.drink;
import static drinkreview.domain.member.QMember.member;
import static drinkreview.domain.order.QOrder.order;
import static drinkreview.domain.orderDrink.QOrderDrink.orderDrink;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderQueryDto> searchPage(Pageable pageable) {
        List<OrderQueryDto> orders = queryFactory
                .select(new QOrderQueryDto(
                        order.id,
                        order.orderDate,
                        order.status,
                        member.id,
                        member.name,
                        delivery.id,
                        delivery.status
                ))
                .from(order)
                .join(order.member, member)
                .join(order.delivery, delivery)
                .orderBy(order.orderDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> orderIds = orders.stream()
                .map(orderQueryDto -> orderQueryDto.getId())
                .toList();

        List<OrderDrinkResponseDto> orderDrinks = queryFactory
                .select(Projections.constructor(
                        OrderDrinkResponseDto.class,
                        orderDrink.id,
                        orderDrink.order.id,
                        drink.id,
                        orderDrink.orderPrice,
                        orderDrink.count
                ))
                .from(orderDrink)
                .join(orderDrink.drink, drink)
                .where(orderDrink.order.id.in(orderIds))
                .fetch();

        Map<Long, List<OrderDrinkResponseDto>> orderDrinkMap = orderDrinks.stream()
                .collect(Collectors.groupingBy(orderDrinkResponseDto -> orderDrinkResponseDto.getOrderId()));

        orders.forEach(orderQueryDto -> orderQueryDto.setOrderDrinks(orderDrinkMap.get(orderQueryDto.getId())));

        return new PageImpl<>(orders, pageable, orders.size());
    }
}
