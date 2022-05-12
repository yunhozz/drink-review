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
    private List<OrderEntity> orderEntities = new ArrayList<>();

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
