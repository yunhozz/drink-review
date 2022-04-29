package drinkreview.domain.delivery;

import drinkreview.domain.Address;
import drinkreview.domain.TimeEntity;
import drinkreview.domain.order.Order;
import drinkreview.global.enums.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //PREPARING, COMPLETE, CANCELED

    public Delivery(Address address, DeliveryStatus status) {
        this.address = address;
        this.status = status;
    }

    public void cancelDelivery() {
        status = DeliveryStatus.CANCELED;
    }

    /**
     * 연관관계 편의 메소드
     */
    public void setOrder(Order order) {
        this.order = order;
    }
}
