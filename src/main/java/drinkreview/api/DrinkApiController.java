package drinkreview.api;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.dto.DrinkResponseDto;
import drinkreview.domain.drink.repository.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DrinkApiController {

    private final DrinkRepository drinkRepository;

    @GetMapping("/drink")
    public Page<DrinkResponseDto> drinksWithPrice(@RequestParam("price") int price, Pageable pageable) {
        Page<Drink> page = drinkRepository.findPageByPrice(price, pageable);
        return page.map(DrinkResponseDto::new);
    }
}
