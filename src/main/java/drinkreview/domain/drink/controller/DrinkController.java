package drinkreview.domain.drink.controller;

import drinkreview.domain.drink.DrinkService;
import drinkreview.domain.drink.dto.DrinkResponseDto;
import drinkreview.domain.order.controller.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class DrinkController {

    private final DrinkService drinkService;

    @GetMapping("/drink/{id}")
    public String viewDrink(@PathVariable("id") Long drinkId, @ModelAttribute OrderForm orderForm, Model model) {
        DrinkResponseDto drink = drinkService.findDrinkDto(drinkId);
        model.addAttribute("drink", drink);

        return "drink/drink-detail";
    }

    @PostConstruct
    public void init() throws Exception {
        for (int i = 1; i <= 30; i++) {
            DrinkForm form = new DrinkForm();
            form.setName("cola" + i);
            form.setCountry("Korea");
            form.setYear(1996);
            form.setMonth(1);
            form.setDay(11);
            form.setPrice(i * 1000);
            form.setStockQuantity(i * 10);

            drinkService.saveDrink(form);
            Thread.sleep(5);
        }
    }
}
