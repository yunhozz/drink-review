package drinkreview.domain.delivery;

import drinkreview.domain.Address;
import drinkreview.domain.TimeEntity;
import drinkreview.domain.order.Order;
import drinkreview.global.enums.City;
import drinkreview.global.enums.DeliveryStatus;
import drinkreview.global.exception.NotShippingException;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //PREPARING, DELIVERING, COMPLETE, CANCELED

    private Delivery(Address address, DeliveryStatus status) {
        this.address = address;
        this.status = status;
    }

    public static Delivery createDelivery(Order order, City city, String street, String etc) {
        Delivery delivery = new Delivery(new Address(city, street, etc), DeliveryStatus.PREPARING);
        delivery.setOrder(order);

        return delivery;
    }

    public void shipping() {
        if (status == DeliveryStatus.PREPARING) {
            status = DeliveryStatus.DELIVERING;

        } else {
            throw new NotShippingException("This delivery of order is already start.");
        }
    }

    public void cancel() {
        if (status == DeliveryStatus.PREPARING) {
            status = DeliveryStatus.CANCELED;

        } else {
            throw new NotShippingException("Delivery is already started.");
        }
    }

    //연관관계 편의 메소드
    private void setOrder(Order order) {
        this.order = order;
        order.setDelivery(this);
    }
}
