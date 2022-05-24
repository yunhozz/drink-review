package drinkreview.global.controller;

import drinkreview.domain.drink.dto.DrinkSimpleResponseDto;
import drinkreview.domain.drink.repository.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final DrinkRepository drinkRepository;

    @GetMapping("/shop")
    public String drinkList2(Model model) {
        List<DrinkSimpleResponseDto> drinks = drinkRepository.searchSimpleDrink();
        model.addAttribute("drinks", drinks);

        return "shop";
    }
}
