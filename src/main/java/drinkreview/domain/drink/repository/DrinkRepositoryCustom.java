package drinkreview.domain.drink.repository;

import drinkreview.domain.drink.dto.DrinkSimpleResponseDto;

import java.util.List;

public interface DrinkRepositoryCustom {

    List<DrinkSimpleResponseDto> searchSimpleDrink();
}
