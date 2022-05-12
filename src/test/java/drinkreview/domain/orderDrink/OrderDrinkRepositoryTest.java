package drinkreview.domain.orderDrink;

import drinkreview.domain.drink.Drink;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderDrinkRepositoryTest {

    @Autowired OrderDrinkRepository orderDrinkRepository;
    @Autowired EntityManager em;

    @Test
    void findOrderThanDrinkStock() throws Exception {
        //given
        Drink drink = Drink.builder()
                .name("cola")
                .country("Korea")
                .productionDate(LocalDate.of(1996, 1, 11))
                .price(10000)
                .image(null)
                .gpa(4.6)
                .stockQuantity(1)
                .build();

        em.persist(drink);

        OrderDrink orderDrink1 = OrderDrink.createOrderDrink(drink, 1);
        OrderDrink orderDrink2 = OrderDrink.createOrderDrink(drink, 2);

        //when
        orderDrinkRepository.save(orderDrink1);
        orderDrinkRepository.save(orderDrink2);

        List<OrderDrink> result = orderDrinkRepository.findOrderThanDrinkStock();

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).contains(orderDrink2);
    }
}