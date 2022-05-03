package drinkreview.domain.delivery.dto;

import drinkreview.domain.Address;
import drinkreview.domain.delivery.Delivery;
import drinkreview.global.enums.City;
import drinkreview.global.enums.DeliveryStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryRequestDto {

    private City city;
    private String street;
    private String etc;

    public Delivery toEntity() {
        Address address = new Address(city, street, etc);
        return new Delivery(address, DeliveryStatus.PREPARING);
    }
}
