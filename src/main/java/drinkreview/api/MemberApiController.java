package drinkreview.api;

import drinkreview.domain.member.Member;
import drinkreview.domain.member.MemberSearchCondition;
import drinkreview.domain.member.dto.MemberQueryDto;
import drinkreview.domain.member.dto.MemberResponseDto;
import drinkreview.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/p1")
    public Page<MemberResponseDto> findMembers(Pageable pageable) {
        Page<Member> page = memberRepository.findPage(pageable);
        Page<MemberResponseDto> pageDto = page.map(MemberResponseDto::new);

        return pageDto;
    }

    @GetMapping("/members/p2")
    public Page<MemberResponseDto> findMembersWithAge(@RequestParam("age") int age, Pageable pageable) {
        Page<Member> page = memberRepository.findPageByAge(age, pageable);
        Page<MemberResponseDto> pageDto = page.map(MemberResponseDto::new);

        return pageDto;
    }

    @GetMapping("/members/p3")
    public Page<MemberResponseDto> findMembersWithAuth(@RequestParam("auth") String auth, Pageable pageable) {
        Page<Member> page = memberRepository.findPageByAuth(auth, pageable);
        Page<MemberResponseDto> pageDto = page.map(MemberResponseDto::new);

        return pageDto;
    }

    @GetMapping("/member/page")
    public Page<MemberQueryDto> searchMemberWithPage(Pageable pageable) {
        return memberRepository.searchByPage(pageable);
    }

    @GetMapping("/member/cond")
    public List<MemberQueryDto> searchMemberWithCond(MemberSearchCondition condition, String memberId, String name, Integer age, Integer ageGoe, Integer ageLoe, String auth) {
        condition.setMemberId(memberId);
        condition.setName(name);
        condition.setAge(age);
        condition.setAgeGoe(ageGoe);
        condition.setAgeLoe(ageLoe);
        condition.setAuth(auth);

        return memberRepository.searchByCondition(condition);
    }

    @GetMapping("/member/c-page")
    public Page<MemberQueryDto> searchMemberWithCondPage(MemberSearchCondition condition, String memberId, String name, Integer age, Integer ageGoe, Integer ageLoe, String auth, Pageable pageable) {
        condition.setMemberId(memberId);
        condition.setName(name);
        condition.setAge(age);
        condition.setAgeGoe(ageGoe);
        condition.setAgeLoe(ageLoe);
        condition.setAuth(auth);

        return memberRepository.searchByConditionPage(condition, pageable);
    }

//    @PostConstruct
    public void init() {
        for (int i = 1; i <= 50; i++) {
            memberRepository.save(new Member("qkrdbsgh" + i, "yh" + i, i));
        }

        for (int i = 51; i <= 100; i++) {
            memberRepository.save(new Member("qkrdbsgh" + i, "yh" + i, i));
        }
    }
}
