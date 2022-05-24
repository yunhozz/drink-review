package drinkreview.domain.order.history;

import drinkreview.domain.TimeEntity;
import drinkreview.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistory extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_history_id")
    private List<OrderEntity> orderEntities = new ArrayList<>(); //orderIds

    @Embedded
    private MemberInfo memberInfo;

    private OrderHistory(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public static OrderHistory createOrderHistory(Member member, OrderEntity orderEntity) {
        OrderHistory orderHistory = new OrderHistory(new MemberInfo(member.getId(), member.getMemberId(), member.getName()));
        orderHistory.orderEntities.add(orderEntity);

        return orderHistory;
    }

    public void updateOrderEntity(OrderEntity orderEntity) {
        this.orderEntities.add(orderEntity);
    }
}

/*
< cascadeType.REMOVE vs. orphanRemoval = true >

    1. cascadeType.REMOVE: 부모 엔티티가 삭제되면 자식 엔티티도 삭제된다. 한편, 이 옵션의 경우 부모 엔티티가 자식 엔티티와의 관계를 제거해도
                           자식 엔티티는 삭제되지 않고 그대로 남아있다.
    2. orphanRemoval = true: 이 또한 부모 엔티티가 삭제되면 자식 엔티티도 삭제된다. 하지만, 부모 엔티티가 자식 엔티티와의 관계를 제거하면
                             자식은 고아로 취급되어 그대로 사라진다.
 */
