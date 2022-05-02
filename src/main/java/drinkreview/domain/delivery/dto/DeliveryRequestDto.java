package drinkreview.domain.delivery.dto;

import drinkreview.domain.Address;
import drinkreview.domain.delivery.Delivery;
import drinkreview.global.enums.City;
import drinkreview.global.enums.DeliveryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryRequestDto {

    private City city;
    private String street;
    private String etc;

    public DeliveryRequestDto(City city, String street, String etc) {
        this.city = city;
        this.street = street;
        this.etc = etc;
    }

    public Delivery toEntity() {
        Address address = new Address(city, street, etc);
        return new Delivery(address, DeliveryStatus.PREPARING);
    }
}
