package drinkreview.domain.member;

import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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

    //회원 탈퇴
    public void withdraw(Long memberId) {
        Member member = this.findMember(memberId);
        memberRepository.delete(member);
    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Member is null."));
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
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
