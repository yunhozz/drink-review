package drinkreview.domain.drink;

import drinkreview.domain.drink.dto.DrinkForm;
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
        DrinkForm drinkForm1 = createDrinkDto("cola", "Korea", 1996, 1, 11, 1000, 10);
        DrinkForm drinkForm2 = createDrinkDto("cider", "USA", 1998, 1, 11, 2000, 20);
        DrinkForm drinkForm3 = createDrinkDto("coffee", "Japan", 1990, 1, 11, 3000, 30);

        //when
        Long drinkId1 = drinkService.saveDrink(drinkForm1);
        Long drinkId2 = drinkService.saveDrink(drinkForm2);
        Long drinkId3 = drinkService.saveDrink(drinkForm3);

        Drink drink1 = drinkRepository.findById(drinkId1).get();
        Drink drink2 = drinkRepository.findById(drinkId2).get();
        Drink drink3 = drinkRepository.findById(drinkId3).get();

        //then
        assertThat(drink1.getId()).isEqualTo(drinkId1);
        assertThat(drink2.getId()).isEqualTo(drinkId2);
        assertThat(drink3.getId()).isEqualTo(drinkId3);
    }


    private DrinkForm createDrinkDto(String name, String country, int year, int month, int day, int price, int stockQuantity) {
        DrinkForm form = new DrinkForm();
        form.setName(name);
        form.setCountry(country);
        form.setYear(year);
        form.setMonth(month);
        form.setDay(day);
        form.setPrice(price);
        form.setStockQuantity(stockQuantity);

        return form;
    }
}