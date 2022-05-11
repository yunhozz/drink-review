package drinkreview.domain.orderDrink;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.DrinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderDrinkService {

    private final OrderDrinkRepository orderDrinkRepository;
    private final DrinkRepository drinkRepository;

    public Long makeOrderDrink(Long drinkId, int count) {
        Drink drink = drinkRepository.findById(drinkId)
                .orElseThrow(() -> new IllegalStateException("Drink is null."));

        OrderDrink orderDrink = OrderDrink.createOrderDrink(drink, count);
        orderDrinkRepository.save(orderDrink);

        return orderDrink.getId();
    }

    public void cancelOrderDrink(Long orderDrinkId) {
        OrderDrink orderDrink = this.findOrderDrink(orderDrinkId);
        orderDrinkRepository.delete(orderDrink);
    }

    @Transactional(readOnly = true)
    private OrderDrink findOrderDrink(Long orderDrinkId) {
        return orderDrinkRepository.findById(orderDrinkId)
                .orElseThrow(() -> new IllegalStateException("OrderDrink is null."));
    }
}
