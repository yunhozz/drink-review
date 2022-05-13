package drinkreview.domain.member.repository;

import drinkreview.domain.delivery.Delivery;
import drinkreview.domain.drink.Drink;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.MemberSearchCondition;
import drinkreview.domain.member.dto.MemberQueryDto;
import drinkreview.domain.order.Order;
import drinkreview.domain.orderDrink.OrderDrink;
import drinkreview.global.enums.City;
import drinkreview.global.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    void save() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 28, "USER");

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member1, member2);
    }

    @Test
    void findByMemberId() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 28, "USER");

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember = memberRepository.findByMemberId("qkrdbsgh1").get();

        //then
        assertThat(findMember.getId()).isEqualTo(member1.getId());
        assertThat(findMember.getMemberId()).isEqualTo("qkrdbsgh1");
        assertThat(findMember.getMemberPw()).isEqualTo("111");
        assertThat(findMember.getName()).isEqualTo("yunho1");
    }

    @Test
    void findMemberIdList() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 28, "USER");

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> result = memberRepository.findMemberIdList();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains("qkrdbsgh1", "qkrdbsgh2");
    }

    @Test
    void findWithNameAndAge() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho1", 27, "USER");
        Member member3 = createMember("qkrdbsgh3", "333", "yunho2", 28, "USER");

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> result = memberRepository.findWithNameAndAge("yunho1", 27);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member1, member2);
        assertThat(result).doesNotContain(member3);
    }

    @Test
    void findWithNames() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 28, "USER");
        Member member3 = createMember("qkrdbsgh3", "333", "yunho3", 29, "USER");

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> result = memberRepository.findWithNames(List.of("yunho1", "yunho2"));

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member1, member2);
        assertThat(result).doesNotContain(member3);
    }

    @Test
    void isOrdering() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 28, "USER");
        Member member3 = createMember("qkrdbsgh3", "333", "yunho3", 29, "USER");

        Drink drink = createDrink("cola", 1000, 100);
        em.persist(drink);

        OrderDrink orderDrink1 = createOrderDrink(drink, 1);
        OrderDrink orderDrink2 = createOrderDrink(drink, 2);
        OrderDrink orderDrink3 = createOrderDrink(drink, 3);
        em.persist(orderDrink1);
        em.persist(orderDrink2);
        em.persist(orderDrink3);

        Order order1 = createOrder(member1, List.of(orderDrink1));
        Order order2 = createOrder(member2, List.of(orderDrink2));
        Order order3 = createOrder(member3, List.of(orderDrink3));
        em.persist(order1);
        em.persist(order2);
        em.persist(order3);

        Delivery delivery1 = createDelivery(order1);
        Delivery delivery2 = createDelivery(order2);
        Delivery delivery3 = createDelivery(order3);
        em.persist(delivery1);
        em.persist(delivery2);
        em.persist(delivery3);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        order2.cancel();
        em.flush();
        em.clear();

        Boolean result1 = memberRepository.isOrdering(member1.getId());
        Boolean result2 = memberRepository.isOrdering(member2.getId());
        Boolean result3 = memberRepository.isOrdering(member3.getId());

        //then
        assertThat(order2.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(result1).isTrue();
        assertThat(result3).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void findPage() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 27, "USER");
        Member member3 = createMember("qkrdbsgh3", "333", "yunho3", 27, "USER");
        Member member4 = createMember("qkrdbsgh4", "444", "yunho4", 30, "ADMIN");
        Member member5 = createMember("qkrdbsgh5", "555", "yunho5", 30, "USER");

        PageRequest pageRequest = PageRequest.of(1, 3);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        Page<Member> result = memberRepository.findPage(pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getContent()).extracting("name")
                .containsExactly("yunho4", "yunho5");
    }

    @Test
    void findPageByAge() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 30, "USER");
        Member member3 = createMember("qkrdbsgh3", "333", "yunho3", 27, "USER");
        Member member4 = createMember("qkrdbsgh4", "444", "yunho4", 30, "ADMIN");
        Member member5 = createMember("qkrdbsgh5", "555", "yunho5", 27, "USER");

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        Page<Member> result = memberRepository.findPageByAge(27, pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent()).extracting("name")
                .containsExactly("yunho1", "yunho3", "yunho5");
    }

    @Test
    void findPageByAuth() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 27, "USER");
        Member member3 = createMember("qkrdbsgh3", "333", "yunho3", 27, "USER");
        Member member4 = createMember("qkrdbsgh4", "444", "yunho4", 30, "ADMIN");
        Member member5 = createMember("qkrdbsgh5", "555", "yunho5", 30, "USER");

        PageRequest pageRequest = PageRequest.of(0, 3);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        Page<Member> result = memberRepository.findPageByAuth("USER", pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent()).extracting("name")
                .containsExactly("yunho2", "yunho3", "yunho5");
    }

    @Test
    void searchByCondition() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 28, "USER");
        Member member3 = createMember("qkrdbsgh3", "333", "yunho3", 29, "USER");

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(27);
        condition.setAgeLoe(29);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<MemberQueryDto> result = memberRepository.searchByCondition(condition);

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    void searchByPage() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 28, "USER");
        Member member3 = createMember("qkrdbsgh3", "333", "yunho3", 29, "USER");
        Member member4 = createMember("qkrdbsgh4", "444", "yunho4", 30, "ADMIN");
        Member member5 = createMember("qkrdbsgh5", "555", "yunho5", 31, "USER");

        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by("name"));

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        Page<MemberQueryDto> result = memberRepository.searchByPage(pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getContent()).extracting("name")
                .containsExactly("yunho4", "yunho5");
    }

    @Test
    void searchByConditionPage() throws Exception {
        //given
        Member member1 = createMember("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        Member member2 = createMember("qkrdbsgh2", "222", "yunho2", 28, "USER");
        Member member3 = createMember("qkrdbsgh3", "333", "yunho3", 29, "USER");
        Member member4 = createMember("qkrdbsgh4", "444", "yunho4", 30, "ADMIN");
        Member member5 = createMember("qkrdbsgh5", "555", "yunho5", 31, "USER");

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(28);
        condition.setAgeLoe(30);

        PageRequest pageRequest = PageRequest.of(0, 2);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        Page<MemberQueryDto> result = memberRepository.searchByConditionPage(condition, pageRequest);

        //then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent()).extracting("name")
                .containsExactly("yunho2", "yunho3");
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
        return new OrderDrink(drink, count);
    }

    private Drink createDrink(String name, int price, int stockQuantity) {
        return Drink.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
}