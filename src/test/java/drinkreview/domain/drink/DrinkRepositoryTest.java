package drinkreview.domain.drink;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DrinkRepositoryTest {

    @Autowired DrinkRepository drinkRepository;
    @Autowired EntityManager em;

    @Test
    void findDrinksOfCountry() throws Exception {
        //given
        Drink drink1 = createDrink("Tea", "Korea", LocalDate.of(1996, 1, 11), 1000, 4.6, 10);
        Drink drink2 = createDrink("coffee", "Korea", LocalDate.of(2000, 1, 11), 2000, 4.5, 20);
        Drink drink3 = createDrink("cola", "USA", LocalDate.of(2004, 1, 11), 3000, 4.4, 30);
        Drink drink4 = createDrink("cider", "USA", LocalDate.of(2008, 1, 11), 4000, 4.3, 40);

        //when
        drinkRepository.save(drink1);
        drinkRepository.save(drink2);
        drinkRepository.save(drink3);
        drinkRepository.save(drink4);

        List<Drink> result = drinkRepository.findDrinksOfCountry("Korea");

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(drink1, drink2);
        assertThat(result).extracting("name").containsExactly("Tea", "coffee");
    }

    @Test
    void findGpaAboveAvg() throws Exception {
        //given
        Drink drink1 = createDrink("Tea", "Korea", LocalDate.of(1996, 1, 11), 1000, 10, 10);
        Drink drink2 = createDrink("coffee", "Korea", LocalDate.of(2000, 1, 11), 2000, 7, 20);
        Drink drink3 = createDrink("cola", "USA", LocalDate.of(2004, 1, 11), 3000, 3, 30);
        Drink drink4 = createDrink("cider", "USA", LocalDate.of(2008, 1, 11), 4000, 0, 40);

        //when
        drinkRepository.save(drink1);
        drinkRepository.save(drink2);
        drinkRepository.save(drink3);
        drinkRepository.save(drink4);

        List<Drink> result = drinkRepository.findGpaAboveAvg(); //avg = 5

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(drink1, drink2);
        assertThat(result).extracting("name").containsExactly("Tea", "coffee");
    }

    @Test
    void findPageByPrice() throws Exception {
        //given
        Drink drink1 = createDrink("Tea", "Korea", LocalDate.of(1996, 1, 11), 1000, 10, 10);
        Drink drink2 = createDrink("coffee", "Korea", LocalDate.of(2000, 1, 11), 1000, 7, 20);
        Drink drink3 = createDrink("cola", "USA", LocalDate.of(2004, 1, 11), 2000, 3, 30);
        Drink drink4 = createDrink("cider", "USA", LocalDate.of(2008, 1, 11), 2000, 0, 40);

        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "price"));

        //when
        drinkRepository.save(drink1);
        drinkRepository.save(drink2);
        drinkRepository.save(drink3);
        drinkRepository.save(drink4);

        Page<Drink> result = drinkRepository.findPageByPrice(1000, pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result).contains(drink1, drink2);
        assertThat(result).extracting("name").containsExactly("Tea", "coffee");
    }

    private Drink createDrink(String name, String country, LocalDate productionDate, int price, double gpa, int stockQuantity) {
        return Drink.builder()
                .name(name)
                .country(country)
                .productionDate(productionDate)
                .price(price)
                .gpa(gpa)
                .stockQuantity(stockQuantity)
                .build();
    }
}