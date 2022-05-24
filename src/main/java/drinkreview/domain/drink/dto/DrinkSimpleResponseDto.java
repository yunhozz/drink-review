package drinkreview.domain.drink.dto;

import drinkreview.domain.drink.Drink;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DrinkSimpleResponseDto {

    private Long id;
    private String name;
    private int price;
    private Byte[] image;
    private double gpa;

    public DrinkSimpleResponseDto(Drink drink) {
        id = drink.getId();
        name = drink.getName();
        price = drink.getPrice();
        image = drink.getImage();
        gpa = drink.getGpa();
    }
}
