package drinkreview.domain.drink;

import drinkreview.domain.TimeEntity;
import drinkreview.global.enums.DrinkStatus;
import drinkreview.global.exception.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Drink extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String country;
    private LocalDate productionDate;
    private int price;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Byte[] image;

    private double gpa;
    private int stockQuantity;
    private int evaluationCount;

    @Enumerated(EnumType.STRING)
    private DrinkStatus status; //ON_SALE, OUT_OF_STOCK

    @Builder
    public Drink(String name, String country, LocalDate productionDate, int price, Byte[] image, double gpa, int stockQuantity, int evaluationCount, DrinkStatus status) {
        this.name = name;
        this.country = country;
        this.productionDate = productionDate;
        this.price = price;
        this.image = image;
        this.gpa = gpa;
        this.stockQuantity = stockQuantity;
        this.evaluationCount = evaluationCount;
        this.status = status;
    }

    public void updateImage(MultipartFile file) throws Exception {
        Byte[] image = new Byte[file.getBytes().length];
        int i = 0;

        for (Byte b : image) {
            image[i++] = b;
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

        if (remainQuantity == 0) {
            status = DrinkStatus.OUT_OF_STOCK;
            this.stockQuantity = 0;
        } else {
            this.stockQuantity = remainQuantity;
        }
    }
}
