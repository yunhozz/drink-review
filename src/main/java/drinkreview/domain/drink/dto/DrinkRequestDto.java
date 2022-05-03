package drinkreview.domain.drink.dto;

import drinkreview.domain.drink.Drink;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DrinkRequestDto {

    private String name;
    private String country;
    private int year;
    private int month;
    private int day;
    private int price;
    private int stockQuantity;

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
