package drinkreview.domain.drink.repository;

import drinkreview.domain.drink.dto.DrinkSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DrinkRepositoryCustom {

    List<DrinkSimpleResponseDto> searchSimpleDrink();
    Page<DrinkSimpleResponseDto> searchSimplePageDrink(Pageable pageable);
    Page<DrinkSimpleResponseDto> searchSimplePageDrinkByKeyword(String keyword, Pageable pageable);
}
