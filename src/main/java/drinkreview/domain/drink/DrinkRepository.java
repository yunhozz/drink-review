package drinkreview.domain.drink;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrinkRepository extends JpaRepository<Drink, Long> {

    //특정 나라의 음료 리스트 조회
    @Query("select d from Drink d where d.country = :country")
    List<Drink> findDrinksOfCountry(@Param("country") String country);

    //평균 이상의 gpa 를 가진 음료 리스트 조회
    @Query("select d from Drink d where d.gpa >= (select avg(d1.gpa) from Drink d1)")
    List<Drink> findGpaAboveAvg();

    @Query("select d from Drink d")
    Page<Drink> findPageByPrice(int price, Pageable pageable);
}
