package drinkreview.domain.order.repository;

import drinkreview.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    @Query(value = "select o from Order o join fetch o.member m",
            countQuery = "select count(o) from Order o")
    Page<Order> findPage(Pageable pageable);

    @Query("select o from Order o where o.status = 'ORDER'")
    List<Order> findCompleteOrder();
}
