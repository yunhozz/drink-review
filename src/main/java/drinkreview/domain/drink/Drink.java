package drinkreview.domain.drink;

import drinkreview.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Drink extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "drink_id")
    private Long id;

    private String name;
    private String country;
    private LocalDate productionDate;
    private int stockQuantity;
}
