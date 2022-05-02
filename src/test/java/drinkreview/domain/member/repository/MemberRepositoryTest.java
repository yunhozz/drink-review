package drinkreview.domain.member.repository;

import drinkreview.domain.member.Member;
import drinkreview.domain.member.MemberSearchCondition;
import drinkreview.domain.member.dto.MemberQueryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

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
        condition.setAgeLoe(28);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("name"));

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
                .containsExactly("yunho1", "yunho2");
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
}