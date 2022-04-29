package drinkreview.domain;

import drinkreview.global.enums.City;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Enumerated(EnumType.STRING)
    private City city;

    private String street;
    private String etc;

    public Address(City city, String street, String etc) {
        this.city = city;
        this.street = street;
        this.etc = etc;
    }
}
