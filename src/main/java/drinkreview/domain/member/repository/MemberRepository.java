package drinkreview.domain.member.repository;

import drinkreview.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByMemberId(String memberId);

    @Query("select m.memberId from Member m")
    List<String> findMemberIdList();

    @Query("select m from Member m where m.name = :name and m.age = :age")
    List<Member> findWithNameAndAge(@Param("name") String name, @Param("age") int age);

    @Query("select m from Member m where m.name in :names")
    List<Member> findWithNames(@Param("names") Collection<String> names);
}
