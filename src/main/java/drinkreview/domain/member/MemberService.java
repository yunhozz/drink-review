package drinkreview.domain.member;

import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //회원 가입
    public Long join(MemberRequestDto memberRequestDto) {
        Member member = memberRequestDto.toEntity();
        validateDuplicated(member); //아이디 중복 검사
        memberRepository.save(member);

        return member.getId();
    }

    //로그인
    @Transactional(readOnly = true)
    public Member login(String memberId, String memberPw) {
        Member member = this.loadUserByUsername(memberId);
        return member.getMemberPw().equals(memberPw) ? member : null;
    }

    //회원 탈퇴
    public void withdraw(Long id) {
        Member member = this.findMember(id);
        memberRepository.delete(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Member loadUserByUsername(String memberId) throws UsernameNotFoundException {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(memberId));
    }

    @Transactional(readOnly = true)
    public Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member is null."));
    }

    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    private void validateDuplicated(Member member) {
        if (this.findMembers().stream().anyMatch(m -> m.getMemberId().equals(member.getMemberId()))) {
            throw new IllegalStateException("There is duplicated member ID.");
        }
    }
}
