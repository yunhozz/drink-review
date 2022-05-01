package drinkreview.domain.order;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.delivery.Delivery;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDrink> orderDrinks = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //ORDER, CANCEL

    private Order(LocalDateTime orderDate, OrderStatus status) {
        this.orderDate = orderDate;
        this.status = status;
    }

    public static Order createOrder(Delivery delivery, List<OrderDrink> orderDrinks) {
        Order order = new Order(LocalDateTime.now(), OrderStatus.ORDER);
        order.setDelivery(delivery);

        for (OrderDrink orderDrink : orderDrinks) {
            order.setOrderDrink(orderDrink);
        }

        return order;
    }

    //주문 수량 변경
    public void updateCountOfDrink(OrderDrink orderDrink, int count) {
        if (orderDrinks.stream().anyMatch(o -> o.getId().equals(orderDrink.getId()))) {
            if (orderDrink.getCount() == count) {
                throw new NotAllowedUpdateOrderException("Order count is same.");
            }

            orderDrink.updateOrder(count);

        } else {
            throw new NotAllowedUpdateOrderException("There's not order.");
        }
    }

    //주문 취소
    public void cancelOrder() {
        if (status == OrderStatus.CANCEL || delivery.getStatus() == DeliveryStatus.COMPLETE) {
            throw new NotAllowedCancelOrderException("This order can't be canceled.");
        }

        for (OrderDrink orderDrink : orderDrinks) {
            orderDrink.cancelOrder();
        }

        delivery.cancelDelivery();
        status = OrderStatus.CANCEL;
    }

    /**
     * 연관관계 편의 메소드
     */
    private void setOrderDrink(OrderDrink orderDrink) {
        orderDrinks.add(orderDrink);
        orderDrink.setOrder(this);
    }

    /**
     * 연관관계 편의 메소드
     */
    private void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
