package drinkreview.domain.member.service;

import drinkreview.domain.member.Member;
import drinkreview.domain.member.controller.LoginForm;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.dto.MemberResponseDto;
import drinkreview.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserDetailsServiceImpl userDetailsService;

    //회원 가입
    public Long join(MemberRequestDto memberRequestDto) {
        Member member = memberRequestDto.toEntity();
        validateDuplicated(member); //아이디 중복 검사
        memberRepository.save(member);

        return member.getId();
    }

    //로그인
    public Member login(LoginForm loginForm) {
        Member member = memberRepository.findByMemberId(loginForm.getMemberId())
                .orElseThrow(() -> new IllegalStateException("Please insert ID again."));

        if (!loginForm.getMemberPw().equals(member.getMemberPw())) {
            throw new IllegalStateException("The password is different. Please enter it again.");
        }

        userDetailsService.loadUserByUsername(member.getMemberId());

        return member;
    }

    //회원 탈퇴
    public void withdraw(Long id) {
        Member member = this.findMember(id);
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMemberDto(Long id) {
        Member member = this.findMember(id);
        return new MemberResponseDto(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findMembersDto() {
        List<Member> members = this.findMembers();
        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();

        for (Member member : members) {
            memberResponseDtoList.add(new MemberResponseDto(member));
        }

        return memberResponseDtoList;
    }

    @Transactional(readOnly = true)
    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member is null."));
    }

    @Transactional(readOnly = true)
    private List<Member> findMembers() {
        return memberRepository.findAll();
    }

    private void validateDuplicated(Member member) {
        if (this.findMembers().stream().anyMatch(m -> m.getMemberId().equals(member.getMemberId()))) {
            throw new IllegalStateException("There is duplicated member ID.");
        }
    }
}
