package drinkreview.domain.order.repository;

import drinkreview.domain.order.history.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {
}
