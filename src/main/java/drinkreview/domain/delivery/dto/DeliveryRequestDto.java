package drinkreview.domain.delivery.dto;

import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.order.Order;
import drinkreview.global.enums.City;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DeliveryRequestDto {

    @NotNull private Order order;
    @NotEmpty private City city;
    @NotBlank private String street;
    @NotBlank private String etc;

    public Delivery toEntity() {
        return Delivery.createDelivery(order, city, street, etc);
    }
}
