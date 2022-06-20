package drinkreview.domain.delivery.dto;

import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.order.Order;
import drinkreview.global.enums.City;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class DeliveryRequestDto {

    private Order order;

    @NotEmpty(message = "Please choose city.")
    private City city;

    @NotBlank(message = "Please input street.")
    private String street;

    @NotBlank(message = "Please input etc.")
    private String etc;

    public Delivery toEntity() {
        return Delivery.createDelivery(order, city, street, etc);
    }
}
