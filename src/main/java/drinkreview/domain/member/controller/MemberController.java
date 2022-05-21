package drinkreview.domain.member.controller;

import drinkreview.domain.member.UserDetailsServiceImpl;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public String login(@Valid LoginForm loginForm, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "member/login";
        }

        if (memberService.login(loginForm.getMemberId(), loginForm.getMemberPw())) {
            UserDetails loginMember = userDetailsService.loadUserByUsername(loginForm.getMemberId());
            session.setAttribute("loginMember", loginMember);
        } else {
            return "member/login";
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
