package drinkreview.domain.orderDrink;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public List<OrderDrinkResponseDto> findOrderDrinkDtoList() {
        List<OrderDrinkResponseDto> orderDrinkDtoList = new ArrayList<>();
        List<OrderDrink> orderDrinkList = this.findOrderDrinkList();

        for (OrderDrink orderDrink : orderDrinkList) {
            OrderDrinkResponseDto orderDrinkDto = new OrderDrinkResponseDto(orderDrink);
            orderDrinkDtoList.add(orderDrinkDto);
        }

        return orderDrinkDtoList;
    }

    @Transactional(readOnly = true)
    private OrderDrink findOrderDrink(Long orderDrinkId) {
        return orderDrinkRepository.findById(orderDrinkId)
                .orElseThrow(() -> new IllegalStateException("OrderDrink is null."));
    }

    @Transactional(readOnly = true)
    private List<OrderDrink> findOrderDrinkList() {
        return orderDrinkRepository.findAll();
    }
}
