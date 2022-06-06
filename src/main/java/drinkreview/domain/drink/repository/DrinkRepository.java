package drinkreview.domain.drink.repository;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.orderDrink.OrderDrink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DrinkRepository extends JpaRepository<Drink, Long>, DrinkRepositoryCustom {

    //평점 업데이트
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Drink d set d.gpa = ((d.gpa * d.evaluationCount) + :score) / (d.evaluationCount + 1) where d.id = :drinkId")
    void updateGpa(@Param("drinkId") Long drinkId, @Param("score") double score);

    //리뷰 갯수 업데이트
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Drink d set d.evaluationCount = d.evaluationCount + 1 where d.id = :drinkId")
    void updateEvaluationCount(@Param("drinkId") Long drinkId);

    //특정 유저의 주문 리스트에 있는 음료 이름 조회
    @Query("select distinct d.name from Order o join o.orderDrinks od join od.drink d where o.member.id = :userId")
    List<String> findDrinkNamesInOrder(@Param("userId") Long userId);

    //음료수 이름으로 ID 조회
    @Query("select d.id from Drink d where d.name = :name")
    Optional<Long> findDrinkIdWithName(@Param("name") String name);

    //특정 나라의 음료 리스트 조회
    @Query("select d from Drink d where d.country = :country")
    List<Drink> findDrinksOfCountry(@Param("country") String country);

    //평균 이상의 gpa 를 가진 음료 리스트 조회
    @Query("select d from Drink d where d.gpa >= (select avg(d1.gpa) from Drink d1)")
    List<Drink> findGpaAboveAvg();

    Page<Drink> findPageByPrice(int price, Pageable pageable);
}
