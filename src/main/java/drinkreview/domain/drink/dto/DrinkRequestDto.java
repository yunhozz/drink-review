package drinkreview.domain.drink.dto;

import drinkreview.domain.drink.Drink;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
public class DrinkRequestDto {

    @NotEmpty private String name;
    @NotEmpty private String country;
    @NotEmpty private int year;
    @NotEmpty private int month;
    @NotEmpty private int day;
    @NotEmpty private int price;
    @NotEmpty private int stockQuantity;

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
