package drinkreview.domain.drink.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import drinkreview.domain.drink.dto.DrinkSimpleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @Override
    public Page<DrinkSimpleResponseDto> searchSimplePageDrink(Pageable pageable) {
        List<DrinkSimpleResponseDto> content = queryFactory
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public Page<DrinkSimpleResponseDto> searchSimplePageDrinkByKeyword(String keyword, Pageable pageable) {
        List<DrinkSimpleResponseDto> content = queryFactory
                .select(Projections.constructor(
                        DrinkSimpleResponseDto.class,
                        drink.id,
                        drink.name,
                        drink.price,
                        drink.image,
                        drink.gpa
                ))
                .from(drink)
                .where(drink.name.contains(keyword))
                .orderBy(drink.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }
}
