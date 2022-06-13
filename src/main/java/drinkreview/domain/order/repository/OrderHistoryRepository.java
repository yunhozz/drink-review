package drinkreview.domain.order.repository;

import drinkreview.domain.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    @Query("select distinct oh from OrderHistory oh join fetch oh.orderEntities oe where oh.memberInfo.userId = :userId")
    Optional<OrderHistory> findWithUserId(@Param("userId") Long userId);
}
