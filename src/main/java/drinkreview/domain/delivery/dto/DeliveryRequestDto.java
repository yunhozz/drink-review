package drinkreview.domain.delivery.dto;

import drinkreview.domain.Address;
import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.order.Order;
import drinkreview.global.enums.City;
import drinkreview.global.enums.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRequestDto {

    private Order order;
    private City city;
    private String street;
    private String etc;

    public Delivery toEntity() {
        Address address = new Address(city, street, etc);
        return new Delivery(order, address, DeliveryStatus.PREPARING);
    }
}
