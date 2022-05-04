package drinkreview.domain.member.service;

import drinkreview.domain.member.Member;
import drinkreview.domain.member.UserDetailsImpl;
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
    private final HttpServletRequest httpServletRequest;

    //memberId 가 DB 에 있는지 확인 -> 스프링 시큐리티 세션에 유저 정보 저장
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find this member : " + memberId));

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("member", new MemberSessionResponseDto(member));

        return new UserDetailsImpl(member);
    }
}
