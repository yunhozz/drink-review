package drinkreview.domain.drink.dto;

import drinkreview.domain.drink.Drink;
import drinkreview.global.enums.DrinkStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DrinkResponseDto {

    private Long id;
    private String name;
    private String country;
    private LocalDate productionDate;
    private int price;
    private Byte[] image;
    private double gpa;
    private int stockQuantity;
    private int salesVolume;
    private DrinkStatus status;

    public DrinkResponseDto(Drink drink) {
        this.id = drink.getId();
        this.name = drink.getName();
        this.country = drink.getCountry();
        this.productionDate = drink.getProductionDate();
        this.price = drink.getPrice();
        this.image = drink.getImage();
        this.gpa = drink.getGpa();
        this.stockQuantity = drink.getStockQuantity();
        this.salesVolume = drink.getSalesVolume();
        this.status = drink.getStatus();
    }
}
