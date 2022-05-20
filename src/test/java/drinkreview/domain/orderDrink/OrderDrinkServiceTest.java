package drinkreview.domain.orderDrink;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.DrinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderDrinkServiceTest {

    @Autowired OrderDrinkService orderDrinkService;
    @Autowired OrderDrinkRepository orderDrinkRepository;
    @Autowired DrinkRepository drinkRepository;

    @Test
    void makeOrderDrink() throws Exception {
        //given
        Drink drink = createDrink("cola", "Korea", 1000, 1996, 1, 11, 100);
        drinkRepository.save(drink);

        //when
        Long orderDrinkId = orderDrinkService.makeOrderDrink(drink.getId(), 2);
        OrderDrink orderDrink = orderDrinkRepository.findById(orderDrinkId).get();

        //then
        assertThat(orderDrink.getId()).isEqualTo(orderDrinkId);
        assertThat(orderDrink.getDrink()).isEqualTo(drink);
        assertThat(orderDrink.getOrderPrice()).isEqualTo(2000);
    }

    private Drink createDrink(String name, String country, int year, int month, int day, int price, int stockQuantity) {
        return Drink.builder()
                .name(name)
                .country(country)
                .productionDate(LocalDate.of(year, month, day))
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
}