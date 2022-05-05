package drinkreview.domain.member.controller;

import drinkreview.domain.member.Member;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.service.MemberService;
import drinkreview.domain.member.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("memberDto", new MemberRequestDto());
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberRequestDto memberRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "member/join";
        }

        memberService.join(memberRequestDto);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "member/login";
        }

        Member loginMember = memberService.login(loginForm);
        model.addAttribute("loginMember", loginMember);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler()
                .logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return "redirect:/";
    }
}
