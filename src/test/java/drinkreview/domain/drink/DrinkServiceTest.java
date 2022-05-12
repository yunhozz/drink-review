package drinkreview.domain.drink;

import drinkreview.domain.drink.dto.DrinkRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DrinkServiceTest {

    @Autowired DrinkService drinkService;
    @Autowired DrinkRepository drinkRepository;

    @Test
    void saveDrink() throws Exception {
        //given
        DrinkRequestDto drinkDto1 = createDrinkDto("cola", "Korea", 1996, 1, 11, 1000, 10);
        DrinkRequestDto drinkDto2 = createDrinkDto("cider", "USA", 1998, 1, 11, 2000, 20);
        DrinkRequestDto drinkDto3 = createDrinkDto("coffee", "Japan", 1990, 1, 11, 3000, 30);

        //when
        Long drinkId1 = drinkService.saveDrink(drinkDto1);
        Long drinkId2 = drinkService.saveDrink(drinkDto2);
        Long drinkId3 = drinkService.saveDrink(drinkDto3);

        Drink drink1 = drinkRepository.findById(drinkId1).get();
        Drink drink2 = drinkRepository.findById(drinkId2).get();
        Drink drink3 = drinkRepository.findById(drinkId3).get();

        //then
        assertThat(drink1.getId()).isEqualTo(drinkId1);
        assertThat(drink2.getId()).isEqualTo(drinkId2);
        assertThat(drink3.getId()).isEqualTo(drinkId3);
    }


    private DrinkRequestDto createDrinkDto(String name, String country, int year, int month, int day, int price, int stockQuantity) {
        DrinkRequestDto dto = new DrinkRequestDto();
        dto.setName(name);
        dto.setCountry(country);
        dto.setYear(year);
        dto.setMonth(month);
        dto.setDay(day);
        dto.setPrice(price);
        dto.setStockQuantity(stockQuantity);

        return dto;
    }
}