package drinkreview.domain.order;

import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.drink.Drink;
import drinkreview.domain.member.Member;
import drinkreview.domain.order.dto.OrderQueryDto;
import drinkreview.domain.order.repository.OrderRepository;
import drinkreview.domain.orderDrink.OrderDrink;
import drinkreview.global.enums.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired OrderRepository orderRepository;
    @Autowired EntityManager em;
    Member member1, member2;
    Drink drink1, drink2;

    @BeforeEach
    void beforeEach() {
        member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        member2 = createMember("qkrdbsgh2", "222", "yunho2", 28, "USER");
        em.persist(member1);
        em.persist(member2);

        drink1 = createDrink("cola", 1000, 100);
        drink2 = createDrink("juice", 2000, 100);
        em.persist(drink1);
        em.persist(drink2);
    }

    @Test
    void findPage() throws Exception {
        //given
        OrderDrink orderDrink1 = createOrderDrink(drink1, 1);
        OrderDrink orderDrink2 = createOrderDrink(drink2, 2);
        OrderDrink orderDrink3 = createOrderDrink(drink1, 3);
        OrderDrink orderDrink4 = createOrderDrink(drink2, 4);
        OrderDrink orderDrink5 = createOrderDrink(drink1, 5);
        OrderDrink orderDrink6 = createOrderDrink(drink2, 6);
        em.persist(orderDrink1);
        em.persist(orderDrink2);
        em.persist(orderDrink3);
        em.persist(orderDrink4);
        em.persist(orderDrink5);
        em.persist(orderDrink6);

        Order order1 = createOrder(member1, List.of(orderDrink1));
        Order order2 = createOrder(member1, List.of(orderDrink2));
        Order order3 = createOrder(member1, List.of(orderDrink3));
        Order order4 = createOrder(member2, List.of(orderDrink4));
        Order order5 = createOrder(member2, List.of(orderDrink5));
        Order order6 = createOrder(member2, List.of(orderDrink6));

        Delivery delivery1 = createDelivery(order1);
        Delivery delivery2 = createDelivery(order2);
        Delivery delivery3 = createDelivery(order3);
        Delivery delivery4 = createDelivery(order4);
        Delivery delivery5 = createDelivery(order5);
        Delivery delivery6 = createDelivery(order6);
        em.persist(delivery1);
        em.persist(delivery2);
        em.persist(delivery3);
        em.persist(delivery4);
        em.persist(delivery5);
        em.persist(delivery6);

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);
        orderRepository.save(order6);

        Page<Order> result = orderRepository.findPage(pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result).contains(order1, order2, order3);
    }

    @Test
    void findCompleteOrder() throws Exception {
        //given
        OrderDrink orderDrink1 = createOrderDrink(drink1, 1);
        OrderDrink orderDrink2 = createOrderDrink(drink2, 2);
        OrderDrink orderDrink3 = createOrderDrink(drink1, 3);
        OrderDrink orderDrink4 = createOrderDrink(drink2, 4);
        OrderDrink orderDrink5 = createOrderDrink(drink1, 5);
        OrderDrink orderDrink6 = createOrderDrink(drink2, 6);
        em.persist(orderDrink1);
        em.persist(orderDrink2);
        em.persist(orderDrink3);
        em.persist(orderDrink4);
        em.persist(orderDrink5);
        em.persist(orderDrink6);

        Order order1 = createOrder(member1, List.of(orderDrink1));
        Order order2 = createOrder(member1, List.of(orderDrink2));
        Order order3 = createOrder(member1, List.of(orderDrink3));
        Order order4 = createOrder(member2, List.of(orderDrink4));
        Order order5 = createOrder(member2, List.of(orderDrink5));
        Order order6 = createOrder(member2, List.of(orderDrink6));

        Delivery delivery1 = createDelivery(order1);
        Delivery delivery2 = createDelivery(order2);
        Delivery delivery3 = createDelivery(order3);
        Delivery delivery4 = createDelivery(order4);
        Delivery delivery5 = createDelivery(order5);
        Delivery delivery6 = createDelivery(order6);
        em.persist(delivery1);
        em.persist(delivery2);
        em.persist(delivery3);
        em.persist(delivery4);
        em.persist(delivery5);
        em.persist(delivery6);

        //when
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);
        orderRepository.save(order6);

        order4.cancel();
        order5.cancel();
        order6.cancel();

        List<Order> result = orderRepository.findCompleteOrder();

        //then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).contains(order1, order2, order3);
        assertThat(result).doesNotContain(order4, order5, order6);
    }

    @Test
    void searchPage() throws Exception {
        //given
        OrderDrink orderDrink1 = createOrderDrink(drink1, 1);
        OrderDrink orderDrink2 = createOrderDrink(drink2, 2);
        OrderDrink orderDrink3 = createOrderDrink(drink1, 3);
        OrderDrink orderDrink4 = createOrderDrink(drink2, 4);
        OrderDrink orderDrink5 = createOrderDrink(drink1, 5);
        OrderDrink orderDrink6 = createOrderDrink(drink2, 6);
        em.persist(orderDrink1);
        em.persist(orderDrink2);
        em.persist(orderDrink3);
        em.persist(orderDrink4);
        em.persist(orderDrink5);
        em.persist(orderDrink6);

        Order order1 = createOrder(member1, List.of(orderDrink1));
        Order order2 = createOrder(member1, List.of(orderDrink2));
        Order order3 = createOrder(member1, List.of(orderDrink3));
        Order order4 = createOrder(member2, List.of(orderDrink4));
        Order order5 = createOrder(member2, List.of(orderDrink5));
        Order order6 = createOrder(member2, List.of(orderDrink6));

        Delivery delivery1 = createDelivery(order1);
        Delivery delivery2 = createDelivery(order2);
        Delivery delivery3 = createDelivery(order3);
        Delivery delivery4 = createDelivery(order4);
        Delivery delivery5 = createDelivery(order5);
        Delivery delivery6 = createDelivery(order6);
        em.persist(delivery1);
        em.persist(delivery2);
        em.persist(delivery3);
        em.persist(delivery4);
        em.persist(delivery5);
        em.persist(delivery6);

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);
        orderRepository.save(order6);

        Page<OrderQueryDto> result = orderRepository.searchPage(pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(3);
    }

    private Member createMember(String memberId, String memberPw, String name, int age, String auth) {
        return Member.builder()
                .memberId(memberId)
                .memberPw(memberPw)
                .name(name)
                .age(age)
                .auth(auth)
                .build();
    }

    private Order createOrder(Member member, List<OrderDrink> orderDrinks) {
        return Order.createOrder(member, orderDrinks);
    }

    private Delivery createDelivery(Order order) {
        return Delivery.createDelivery(order, City.SEOUL, "street", "etc");
    }

    private OrderDrink createOrderDrink(Drink drink, int count) {
        return OrderDrink.createOrderDrink(drink, count);
    }

    private Drink createDrink(String name, int price, int stockQuantity) {
        return Drink.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
}