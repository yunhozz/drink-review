package drinkreview.domain.drink.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrinkQueryDto {

    private Long id;
    private String name;
    private int price;
    private Byte[] image;
    private double gpa;

    @QueryProjection
    public DrinkQueryDto(Long id, String name, int price, Byte[] image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    @QueryProjection
    public DrinkQueryDto(Long id, String name, int price, Byte[] image, double gpa) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.gpa = gpa;
    }
}
