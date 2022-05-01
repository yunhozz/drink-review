package drinkreview.domain.drink.dto;

import drinkreview.domain.drink.Drink;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DrinkRequestDto {

    private String name;
    private String country;
    private int year;
    private int month;
    private int day;
    private int price;
    private int stockQuantity;

    @Builder
    private DrinkRequestDto(String name, String country, int year, int month, int day, int price, int stockQuantity) {
        this.name = name;
        this.country = country;
        this.year = year;
        this.month = month;
        this.day = day;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Drink toEntity() {
        return Drink.builder()
                .name(name)
                .country(country)
                .productionDate(LocalDate.of(year, month, day))
                .price(price)
                .stockQuantity(stockQuantity)
                .gpa(0)
                .build();
    }
}
