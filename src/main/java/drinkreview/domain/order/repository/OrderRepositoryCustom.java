package drinkreview.domain.order.repository;

import drinkreview.domain.order.dto.OrderQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

    Page<OrderQueryDto> searchPage(Pageable pageable);
}
