package drinkreview.domain.drink.controller;

import drinkreview.domain.drink.dto.DrinkResponseDto;
import drinkreview.domain.drink.DrinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class DrinkController {

    private final DrinkService drinkService;

    @GetMapping("/drink")
    public String drinkView(@RequestParam("id") Long drinkId, Model model) {
        DrinkResponseDto drink = drinkService.findDrinkDto(drinkId);
        model.addAttribute("drink", drink);

        return "drink/detail";
    }

    @PostConstruct
    public void init() throws Exception {
        for (int i = 1; i <= 8; i++) {
            DrinkForm form = new DrinkForm();
            form.setName("cola" + i);
            form.setCountry("Korea");
            form.setYear(2022);
            form.setMonth(i);
            form.setDay(i * 2);
            form.setPrice(i * 1000);
            form.setStockQuantity(i * 10);

            drinkService.saveDrink(form);
            Thread.sleep(10);
        }
    }
}
