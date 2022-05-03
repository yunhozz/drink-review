package drinkreview.domain.member.controller;

import drinkreview.domain.member.Member;
import drinkreview.domain.member.MemberService;
import drinkreview.domain.member.dto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("memberDto", new MemberRequestDto());
        return "join";
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
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "member/login";
        }

        Member member = memberService.login(loginForm.getMemberId(), loginForm.getMemberPw());
        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.LOGIN_MEMBER, member);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
