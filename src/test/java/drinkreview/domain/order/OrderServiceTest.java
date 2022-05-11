package drinkreview.domain.order;

import drinkreview.domain.Address;
import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.drink.Drink;
import drinkreview.domain.drink.dto.DrinkRequestDto;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.order.repository.OrderRepository;
import drinkreview.domain.orderDrink.OrderDrinkService;
import drinkreview.global.enums.City;
import drinkreview.global.enums.DeliveryStatus;
import drinkreview.global.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderDrinkService orderDrinkService;
    @Autowired EntityManager em;
    Member member;
    Drink drink;

    @BeforeEach
    void beforeEach() {
        MemberRequestDto memberDto = createMemberDto("qkrdbsgh", "111", "yunho", 27, "ADMIN");
        member = memberDto.toEntity();
        em.persist(member);

        DrinkRequestDto drinkDto = createDrinkDto("cola", "Korea", 1996, 1, 11, 2000, 100);
        drink = drinkDto.toEntity();
        em.persist(drink);
    }

    @Test
    void makeOrder() throws Exception {
        //given
        Long id1 = orderDrinkService.makeOrderDrink(drink.getId(), 1);
        Long id2 = orderDrinkService.makeOrderDrink(drink.getId(), 2);
        Long id3 = orderDrinkService.makeOrderDrink(drink.getId(), 3);

        //when
        Long orderId = orderService.makeOrder(member.getId(), List.of(id1, id2, id3));
        Order order = orderRepository.findById(orderId).get();

        //then
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getMember().getId()).isEqualTo(member.getId());
        assertThat(order.getOrderDrinks()).hasSize(3);
    }

    @Test
    void updateOrder() throws Exception {
        //given
        Long id1 = orderDrinkService.makeOrderDrink(drink.getId(), 1);
        Long id2 = orderDrinkService.makeOrderDrink(drink.getId(), 2);
        Long id3 = orderDrinkService.makeOrderDrink(drink.getId(), 3);

        //when
        Long orderId = orderService.makeOrder(member.getId(), List.of(id1, id2, id3));
        Order order = orderRepository.findById(orderId).get();

        Delivery delivery = new Delivery(order, new Address(City.SEOUL, "street", "etc"), DeliveryStatus.PREPARING);
        em.persist(delivery);

        orderService.updateOrder(order.getId(), id2, 4);

        //then
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(order.getOrderDrinks()).extracting("count")
                .containsExactly(1, 4, 3);
    }

    @Test
    void updateOrderFail1() throws Exception {
        //given
        Long id1 = orderDrinkService.makeOrderDrink(drink.getId(), 1);
        Long id2 = orderDrinkService.makeOrderDrink(drink.getId(), 2);
        Long id3 = orderDrinkService.makeOrderDrink(drink.getId(), 3);

        //when
        Long orderId = orderService.makeOrder(member.getId(), List.of(id1, id2, id3));
        Order order = orderRepository.findById(orderId).get();

        Delivery delivery = new Delivery(order, new Address(City.SEOUL, "street", "etc"), DeliveryStatus.PREPARING);
        em.persist(delivery);

        //then
        try {
            orderService.updateOrder(order.getId(), id2, 2);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Order count is same.");
        }
    }

    @Test
    void updateOrderFail2() throws Exception {
        //given
        Long id1 = orderDrinkService.makeOrderDrink(drink.getId(), 1);
        Long id2 = orderDrinkService.makeOrderDrink(drink.getId(), 2);
        Long id3 = orderDrinkService.makeOrderDrink(drink.getId(), 3);

        //when
        Long orderId = orderService.makeOrder(member.getId(), List.of(id1, id2, id3));
        Order order = orderRepository.findById(orderId).get();

        Delivery delivery = new Delivery(order, new Address(City.SEOUL, "street", "etc"), DeliveryStatus.DELIVERING);
        em.persist(delivery);

        //then
        try {
            orderService.updateOrder(order.getId(), id2, 4);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Delivery is already started.");
        }
    }

    @Test
    void cancelOrder() throws Exception {
        //given
        Long id1 = orderDrinkService.makeOrderDrink(drink.getId(), 1);
        Long id2 = orderDrinkService.makeOrderDrink(drink.getId(), 2);
        Long id3 = orderDrinkService.makeOrderDrink(drink.getId(), 3);

        //when
        Long orderId = orderService.makeOrder(member.getId(), List.of(id1, id2, id3));
        Order order = orderRepository.findById(orderId).get();

        Delivery delivery = new Delivery(order, new Address(City.SEOUL, "street", "etc"), DeliveryStatus.PREPARING);
        em.persist(delivery);

        orderService.cancelOrder(order.getId());

        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(order.getOrderDrinks().get(0).getDrink().getStockQuantity()).isEqualTo(100);
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.CANCELED);
    }

    @Test
    void cancelOrderFail() throws Exception {
        //given
        Long id1 = orderDrinkService.makeOrderDrink(drink.getId(), 1);
        Long id2 = orderDrinkService.makeOrderDrink(drink.getId(), 2);
        Long id3 = orderDrinkService.makeOrderDrink(drink.getId(), 3);

        //when
        Long orderId = orderService.makeOrder(member.getId(), List.of(id1, id2, id3));
        Order order = orderRepository.findById(orderId).get();

        Delivery delivery = new Delivery(order, new Address(City.SEOUL, "street", "etc"), DeliveryStatus.DELIVERING);
        em.persist(delivery);

        //then
        try {
            orderService.cancelOrder(order.getId());
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Delivery is already started.");
        }
    }

    private MemberRequestDto createMemberDto(String memberId, String memberPw, String name, int age, String auth) {
        MemberRequestDto dto = new MemberRequestDto();
        dto.setMemberId(memberId);
        dto.setMemberPw(memberPw);
        dto.setName(name);
        dto.setAge(age);
        dto.setAuth(auth);

        return dto;
    }

    private DrinkRequestDto createDrinkDto(String name, String country, int year, int month, int day, int price, int stockQuantity) {
        DrinkRequestDto dto = new DrinkRequestDto();
        dto.setName(name);
        dto.setCountry(country);
        dto.setYear(year);
        dto.setMonth(month);
        dto.setDay(day);
        dto.setPrice(price);
        dto.setStockQuantity(stockQuantity);

        return dto;
    }
}