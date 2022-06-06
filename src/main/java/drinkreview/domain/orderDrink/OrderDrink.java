package drinkreview.domain.orderDrink;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.drink.Drink;
import drinkreview.domain.order.Order;
import drinkreview.global.enums.DeliveryStatus;
import drinkreview.global.enums.DrinkStatus;
import drinkreview.global.exception.NotAllowedUpdateOrderException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDrink extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drink_id")
    private Drink drink;

    private int orderPrice; //음료 하나의 가격 x count = 총 가격
    private int count;

    private OrderDrink(Drink drink, int orderPrice, int count) {
        this.drink = drink;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderDrink createOrderDrink(Drink drink, int count) {
        if (count <= 0) {
            throw new IllegalStateException("Can't order this drink with count lower than one.");
        }

        if (drink.getStatus() == DrinkStatus.OUT_OF_STOCK) {
            throw new IllegalStateException("Drink is out of stock.");
        }

        return new OrderDrink(drink, count * drink.getPrice(), count);
    }

    //주문 수량 변경
    public void updateCountOfDrink(int count) {
        if (order.getDelivery().getStatus() == DeliveryStatus.PREPARING) {
            throw new NotAllowedUpdateOrderException("Delivery is already started.");
        }

        int updatedCount = this.count - count;
        if (updatedCount > 0) {
            drink.addQuantity(updatedCount);
        } else if (updatedCount < 0) {
            drink.removeQuantity(Math.abs(updatedCount));
        } else {
            throw new NotAllowedUpdateOrderException("Order count is same.");
        }

        orderPrice = drink.getPrice() * count;
        this.count = count;
    }

    //주문 접수
    public void createOrder() {
        drink.removeQuantity(count);
    }

    //Order -> 전체 주문 취소
    public void cancelOrder() {
        drink.addQuantity(count);
    }

    //연관관계 편의 메소드
    public void setOrder(Order order) {
        this.order = order;
    }
}
