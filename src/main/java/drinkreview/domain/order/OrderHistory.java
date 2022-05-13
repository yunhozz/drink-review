package drinkreview.domain.order;

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

    private Long userId;
    private String memberId;
    private String name;

    public OrderHistory(Member member, OrderEntity orderEntity) {
        this.userId = member.getId();
        this.memberId = member.getMemberId();
        this.name = member.getName();
        this.orderEntities.add(orderEntity);
    }

    public void update(OrderEntity orderEntity) {
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
