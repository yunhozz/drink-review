package drinkreview.domain.member.controller;

import drinkreview.domain.member.UserDetailsServiceImpl;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/member/join")
    public String join(@Valid MemberRequestDto memberRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "home";
        }

        memberService.join(memberRequestDto);
        return "redirect:/";
    }

    @PostMapping("/member/login")
    public String login(@Valid LoginForm loginForm, BindingResult result, @RequestParam(defaultValue = "/") String redirectURL) {
        if (result.hasErrors()) {
            return "home";
        }

        if (memberService.login(loginForm.getMemberId(), loginForm.getMemberPw())) {
            userDetailsService.loadUserByUsername(loginForm.getMemberId());
        } else {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "home";
        }

        if (redirectURL != null) {
            return "redirect:" + redirectURL;
        }

        return "redirect:/";
    }

    @GetMapping("/member/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
