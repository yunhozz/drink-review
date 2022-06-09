package drinkreview.domain.drink.repository;

import drinkreview.domain.drink.dto.DrinkQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DrinkRepositoryCustom {

    DrinkQueryDto searchDrinkOnCart(Long drinkId);
    Page<DrinkQueryDto> searchSimplePageDrink(Pageable pageable);
    Page<DrinkQueryDto> searchSimplePageDrinkByKeyword(String keyword, Pageable pageable);
}
