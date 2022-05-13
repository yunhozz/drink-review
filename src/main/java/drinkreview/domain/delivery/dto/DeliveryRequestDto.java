package drinkreview.domain.delivery.dto;

import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.order.Order;
import drinkreview.global.enums.City;
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
        return Delivery.createDelivery(order, city, street, etc);
    }
}
