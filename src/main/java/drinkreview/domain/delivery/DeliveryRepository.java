package drinkreview.domain.delivery;

import drinkreview.global.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("select d from Delivery d")
    Page<Delivery> findPage(Pageable pageable);

    Page<Delivery> findPageByStatus(DeliveryStatus status, Pageable pageable);
}
