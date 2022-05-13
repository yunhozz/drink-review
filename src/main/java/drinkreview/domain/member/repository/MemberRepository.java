package drinkreview.domain.member.repository;

import drinkreview.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByMemberId(String memberId);

    //ID 리스트 조회
    @Query("select m.memberId from Member m")
    List<String> findMemberIdList();

    //이름, 나이로 조회
    @Query("select m from Member m where m.name = :name and m.age = :age")
    List<Member> findWithNameAndAge(@Param("name") String name, @Param("age") int age);

    //이름 컬렉션으로 조회
    @Query("select m from Member m where m.name in :names")
    List<Member> findWithNames(@Param("names") Collection<String> names);

    //한건이라도 주문한 고객 조회
    @Query("select m from Member m where (select o from Order o where o.member = m) > 0")
    List<Member> findAtLeastOneOrder();

    //한건이라도 주문상태인 고객인지 ?
    @Query("select case" +
            " when (select o from Order o where o.status = 'ORDER' and o.member = m) > 0 then true" +
            " else false end" +
            " from Member m" +
            " where m.id = :id")
    Boolean isOrdering(@Param("id") Long userId);

    @Query("select m from Member m")
    Page<Member> findPage(Pageable pageable);

    Page<Member> findPageByAge(int age, Pageable pageable);
    Page<Member> findPageByAuth(String auth, Pageable pageable);
}
