package drinkreview.domain.orderDrink;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDrinkRepository extends JpaRepository<OrderDrink, Long> {

    //userId 로 조회
    @Query("select od from OrderDrink od join fetch od.order o join fetch o.member m where m.id = :userId")
    List<OrderDrink> findWithUserId(@Param("userId") Long userId);
}
