package drinkreview.domain.member;

import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.dto.MemberResponseDto;
import drinkreview.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    @Commit
    void join() throws Exception {
        //given
        MemberRequestDto memberDto1 = createMemberDto("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        MemberRequestDto memberDto2 = createMemberDto("qkrdbsgh2", "222", "yunho2", 28, "USER");
        MemberRequestDto memberDto3 = createMemberDto("qkrdbsgh3", "333", "yunho3", 29, "USER");

        //when
        memberService.join(memberDto1);
        memberService.join(memberDto2);
        memberService.join(memberDto3);

        //then
    }

    @Test
    void joinFail() throws Exception {
        //given
        MemberRequestDto memberDto1 = createMemberDto("qkrdbsgh", "111", "yunho1", 27, "ADMIN");
        MemberRequestDto memberDto2 = createMemberDto("qkrdbsgh", "222", "yunho2", 28, "USER");

        //when
        memberService.join(memberDto1);

        //then
        try {
            memberService.join(memberDto2);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("There is duplicated member ID.");
        }
    }

    @Test
    void login() throws Exception {
        //given
        MemberRequestDto memberDto = createMemberDto("qkrdbsgh", "111", "yunho", 27, "ADMIN");

        //when
        memberService.join(memberDto);
        MemberResponseDto loginMember = memberService.login("qkrdbsgh", "111");

        //then
        assertThat(loginMember.getMemberId()).isEqualTo(memberDto.getMemberId());
    }

    @Test
    void loginFail() throws Exception {
        //given
        MemberRequestDto memberDto = createMemberDto("qkrdbsgh", "111", "yunho", 27, "ADMIN");

        //when
        memberService.join(memberDto);

        //then
        try {
            memberService.login("qkrdbsgh", "222");
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("The password is different. Please enter it again.");
        }
    }

    @Test
    void withdraw() throws Exception {
        //given
        MemberRequestDto memberDto1 = createMemberDto("qkrdbsgh1", "111", "yunho1", 27, "ADMIN");
        MemberRequestDto memberDto2 = createMemberDto("qkrdbsgh2", "222", "yunho2", 28, "USER");

        //when
        Long memberId1 = memberService.join(memberDto1);
        Long memberId2 = memberService.join(memberDto2);

        memberService.withdraw(memberId1);

        MemberResponseDto result2 = memberService.findMemberDto(memberId2);

        //then
        assertThat(result2.getMemberId()).isEqualTo(memberDto2.getMemberId());

        try {
            memberService.findMemberDto(memberId1);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Member is null.");
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
}