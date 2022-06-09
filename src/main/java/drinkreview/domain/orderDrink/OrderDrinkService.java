package drinkreview.domain.orderDrink;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderDrinkService {

    private final OrderDrinkRepository orderDrinkRepository;

    public void update(Long orderDrinkId, int count) {
        OrderDrink orderDrink = this.findOrderDrink(orderDrinkId);
        orderDrink.updateCountOfDrink(count);
    }

    @Transactional(readOnly = true)
    public OrderDrinkResponseDto findOrderDrinkDto(Long orderDrinkId) {
        OrderDrink orderDrink = this.findOrderDrink(orderDrinkId);
        return new OrderDrinkResponseDto(orderDrink);
    }

    @Transactional(readOnly = true)
    private OrderDrink findOrderDrink(Long orderDrinkId) {
        return orderDrinkRepository.findById(orderDrinkId)
                .orElseThrow(() -> new IllegalStateException("OrderDrink is null."));
    }
}
