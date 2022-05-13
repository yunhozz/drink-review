package drinkreview.domain.order;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.member.Member;
import drinkreview.domain.orderDrink.OrderDrink;
import drinkreview.global.enums.DeliveryStatus;
import drinkreview.global.enums.OrderStatus;
import drinkreview.global.exception.NotAllowedCancelOrderException;
import drinkreview.global.exception.NotAllowedUpdateOrderException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderDrink> orderDrinks = new ArrayList<>();

    @OneToOne(mappedBy = "order")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //ORDER, CANCEL, COMPLETE

    private Order(Member member, LocalDateTime orderDate, OrderStatus status) {
        this.member = member;
        this.orderDate = orderDate;
        this.status = status;
    }

    public static Order createOrder(Member member, List<OrderDrink> orderDrinks) {
        Order order = new Order(member, LocalDateTime.now(), OrderStatus.ORDER);

        for (OrderDrink orderDrink : orderDrinks) {
            order.setOrderDrink(orderDrink);
            orderDrink.createOrder();
        }

        return order;
    }

    //주문 수량 변경
    public void updateCountOfDrink(OrderDrink orderDrink, int count) {
        if (orderDrinks.stream().anyMatch(o -> o.equals(orderDrink))) {
            if (count == orderDrink.getCount()) {
                throw new NotAllowedUpdateOrderException("Order count is same.");
            }

            if (delivery.getStatus() == DeliveryStatus.PREPARING) {
                orderDrink.updateOrder(count);

            } else {
                throw new NotAllowedUpdateOrderException("Delivery is already started.");
            }

        } else {
            throw new NotAllowedUpdateOrderException("There's not order.");
        }
    }

    public void complete() {
        if (status == OrderStatus.ORDER) {
            status = OrderStatus.COMPLETE;
        }
    }

    //전체 주문 취소
    public void cancel() {
        if (status == OrderStatus.CANCEL) {
            throw new NotAllowedCancelOrderException("This order can't be canceled.");
        }

        for (OrderDrink orderDrink : orderDrinks) {
            orderDrink.cancelOrder();
        }

        delivery.canceled();
        status = OrderStatus.CANCEL;
    }

    public void deleteMember() {
        if (this.member != null) {
            this.member = null;
        }
    }

    //연관관계 편의 메소드
    private void setOrderDrink(OrderDrink orderDrink) {
        orderDrinks.add(orderDrink);
        orderDrink.setOrder(this);
    }

    //연관관계 편의 메소드
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
