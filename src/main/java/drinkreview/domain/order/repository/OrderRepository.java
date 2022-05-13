package drinkreview.domain.order.repository;

import drinkreview.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    //주문 완료된 주문건 조회
    @Query("select o from Order o where o.status = 'ORDER'")
    List<Order> findCompleteOrder();

    //주문 취소된 주문건 조회
    @Query("select o from Order o where o.status = 'CANCEL'")
    List<Order> findCanceledOrder();

    //전체 상품 각각의 재고와 주문량이 같은 주문들
    @Query("select distinct o from Order o join fetch o.orderDrinks od" +
            " where od.count = all(select d.stockQuantity from Drink d)")
    List<Order> findOrderThanDrinkStock();

    @Query(value = "select o from Order o join fetch o.member m",
            countQuery = "select count(o) from Order o")
    Page<Order> findPage(Pageable pageable);
}
