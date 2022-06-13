package drinkreview.domain.order;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.member.Member;
import drinkreview.domain.orderDrink.OrderDrink;
import drinkreview.global.enums.OrderStatus;
import drinkreview.global.exception.NotAllowedCancelOrderException;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDrink> orderDrinks = new ArrayList<>();

    @OneToOne(mappedBy = "order")
    private Delivery delivery;

    @Column(columnDefinition = "datetime(6)")
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
        if (delivery != null) {
            delivery.canceled();
        }
        status = OrderStatus.CANCEL;
    }

    public void deleteMember() {
        if (member != null) {
            member = null;
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
