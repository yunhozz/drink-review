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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final DrinkRepository drinkRepository;

    @GetMapping
    public String drinkView(@PageableDefault(size = 8) Pageable pageable, Model model) {
        Page<DrinkSimpleResponseDto> drinks = drinkRepository.searchSimplePageDrink(pageable);
        model.addAttribute("drinks", drinks);

        return "shop";
    }

    @GetMapping("/drink/list")
    public String drinkList(Model model) {
        List<DrinkSimpleResponseDto> drinks = drinkRepository.searchSimpleDrink();
        model.addAttribute("drinks", drinks);

        return "drink/list";
    }
}
