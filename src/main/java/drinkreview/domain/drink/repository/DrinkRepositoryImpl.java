package drinkreview.domain.drink.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import drinkreview.domain.drink.dto.DrinkQueryDto;
import drinkreview.domain.drink.dto.QDrinkQueryDto;
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
    public DrinkQueryDto searchDrinkOnCart(Long drinkId) {
        return queryFactory
                .select(new QDrinkQueryDto(
                        drink.id,
                        drink.name,
                        drink.price,
                        drink.image
                ))
                .from(drink)
                .where(drink.id.eq(drinkId))
                .fetchOne();
    }

    @Override
    public Page<DrinkQueryDto> searchSimplePageDrink(Pageable pageable) {
        List<DrinkQueryDto> content = queryFactory
                .select(new QDrinkQueryDto(
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

        Long count = queryFactory
                .select(drink.count())
                .from(drink)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<DrinkQueryDto> searchSimplePageDrinkByKeyword(String keyword, Pageable pageable) {
        List<DrinkQueryDto> content = queryFactory
                .select(new QDrinkQueryDto(
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

        Long count = queryFactory
                .select(drink.count())
                .from(drink)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }
}
