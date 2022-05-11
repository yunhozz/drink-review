package drinkreview.domain.delivery.dto;

import drinkreview.domain.delivery.Delivery;
import drinkreview.global.enums.City;
import drinkreview.global.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryResponseDto {

    private Long id;
    private Long orderId;
    private City city;
    private String street;
    private String etc;
    private DeliveryStatus status;

    public DeliveryResponseDto(Delivery delivery) {
        this.id = delivery.getId();
        this.orderId = delivery.getOrder().getId();
        this.city = delivery.getAddress().getCity();
        this.street = delivery.getAddress().getStreet();
        this.etc = delivery.getAddress().getEtc();
        this.status = delivery.getStatus();
    }
}
