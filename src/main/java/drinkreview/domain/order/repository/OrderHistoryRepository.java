package drinkreview.domain.order.repository;

import drinkreview.domain.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    @Query("select oh from OrderHistory oh where oh.memberInfo.userId = :id")
    Optional<OrderHistory> findWithUserId(@Param("id") Long userId);
}
