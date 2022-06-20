package drinkreview.domain.order;

import drinkreview.domain.TimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_history_id")
    private OrderHistory orderHistory;

    private Long orderId;

    public OrderEntity(Long orderId) {
        this.orderId = orderId;
    }

    //연관관계 편의 메소드
    protected void setOrderHistory(OrderHistory orderHistory) {
        this.orderHistory = orderHistory;
    }
}
