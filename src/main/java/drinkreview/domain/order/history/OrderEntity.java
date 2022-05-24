package drinkreview.domain.order.history;

import drinkreview.domain.TimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderEntity extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long orderId;

    public OrderEntity(Long orderId) {
        this.orderId = orderId;
    }
}
