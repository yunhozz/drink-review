package drinkreview.domain.drink.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import drinkreview.domain.drink.dto.DrinkSimpleResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static drinkreview.domain.drink.QDrink.drink;

@RequiredArgsConstructor
public class DrinkRepositoryImpl implements DrinkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DrinkSimpleResponseDto> searchSimpleDrink() {
        return queryFactory
                .select(Projections.constructor(
                        DrinkSimpleResponseDto.class,
                        drink.id,
                        drink.name,
                        drink.price,
                        drink.image,
                        drink.gpa
                ))
                .from(drink)
                .orderBy(drink.createdDate.desc())
                .fetch();
    }
}
