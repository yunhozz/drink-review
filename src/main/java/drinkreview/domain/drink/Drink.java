package drinkreview.domain.drink;

import drinkreview.domain.TimeEntity;
import drinkreview.global.exception.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Drink extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String country;
    private LocalDate productionDate;
    private int price;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Byte[] image;

    private double gpa;
    private int stockQuantity;

    @Builder
    private Drink(String name, String country, LocalDate productionDate, int price, Byte[] image, double gpa, int stockQuantity) {
        this.name = name;
        this.country = country;
        this.productionDate = productionDate;
        this.price = price;
        this.image = image;
        this.gpa = gpa;
        this.stockQuantity = stockQuantity;
    }

    public void updateImage(Byte[] image) {
        if (this.image != null) {
            throw new IllegalStateException("This entity's image field is not null.");
        }

        this.image = image;
    }

    public void addQuantity(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    public void removeQuantity(int stockQuantity) {
        int remainQuantity = this.stockQuantity - stockQuantity;

        if (remainQuantity < 0) {
            throw new NotEnoughStockException("Not enough stock.");
        }

        this.stockQuantity = remainQuantity;
    }
}
