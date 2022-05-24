package drinkreview.domain.member.security;

import drinkreview.domain.member.Member;
import drinkreview.global.controller.SessionConstant;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(memberId));

        HttpSession session = request.getSession();
        session.setAttribute(SessionConstant.LOGIN_MEMBER, new MemberSessionResponseDto(member));

        return new UserDetailsImpl(member);
    }
}
