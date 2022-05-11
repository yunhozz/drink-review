package drinkreview.domain.orderDrink;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDrinkRepository extends JpaRepository<OrderDrink, Long> {

    //전체 상품 각각의 재고보다 주문량이 많은 주문들
    @Query("select od from OrderDrink od where od.count > all(select d.stockQuantity from Drink d)")
    List<OrderDrink> findOrderThanDrinkStock();
}
