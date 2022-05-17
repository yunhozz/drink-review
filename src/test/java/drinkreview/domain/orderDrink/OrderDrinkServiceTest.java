package drinkreview.domain.orderDrink;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.DrinkRepository;
import drinkreview.domain.drink.dto.DrinkRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderDrinkServiceTest {

    @Autowired OrderDrinkService orderDrinkService;
    @Autowired OrderDrinkRepository orderDrinkRepository;
    @Autowired DrinkRepository drinkRepository;

    @Test
    void makeOrderDrink() throws Exception {
        //given
        DrinkRequestDto drinkDto = createDrinkDto("cola", "Korea", 1000, 1996, 1, 11);
        Drink drink = drinkRepository.save(drinkDto.toEntity());

        //when
        Long orderDrinkId = orderDrinkService.makeOrderDrink(drink.getId(), 2);
        OrderDrink orderDrink = orderDrinkRepository.findById(orderDrinkId).get();

        //then
        assertThat(orderDrink.getId()).isEqualTo(orderDrinkId);
        assertThat(orderDrink.getDrink()).isEqualTo(drink);
        assertThat(orderDrink.getOrderPrice()).isEqualTo(2000);
    }

    private DrinkRequestDto createDrinkDto(String name, String country, int price, int year, int month, int day) {
        DrinkRequestDto dto = new DrinkRequestDto();
        dto.setName(name);
        dto.setCountry(country);
        dto.setPrice(price);
        dto.setYear(year);
        dto.setMonth(month);
        dto.setDay(day);

        return dto;
    }
}