package drinkreview.domain.drink.repository;

import drinkreview.domain.drink.dto.DrinkSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DrinkRepositoryCustom {

    Page<DrinkSimpleResponseDto> searchSimplePageDrink(Pageable pageable);
    Page<DrinkSimpleResponseDto> searchSimplePageDrinkByKeyword(String keyword, Pageable pageable);
}
