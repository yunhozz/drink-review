package drinkreview.global.controller;

import drinkreview.domain.drink.dto.DrinkSimpleResponseDto;
import drinkreview.domain.drink.repository.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final DrinkRepository drinkRepository;

    @GetMapping("/shop")
    public String drinkList(@PageableDefault(size = 8) Pageable pageable, Model model) {
        Page<DrinkSimpleResponseDto> drinks = drinkRepository.searchSimplePageDrink(pageable);
        model.addAttribute("drinks", drinks);

        return "shop";
    }
}
