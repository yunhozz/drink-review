package drinkreview.domain.order.repository;

import drinkreview.domain.order.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    Optional<OrderHistory> findByUserId(Long userId);
}
