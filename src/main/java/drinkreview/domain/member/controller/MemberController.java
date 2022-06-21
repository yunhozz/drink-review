package drinkreview.domain.member.controller;

import drinkreview.domain.member.MemberService;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.dto.MemberResponseDto;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.member.UserDetailsServiceImpl;
import drinkreview.global.controller.SessionConstant;
import drinkreview.global.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/join")
    public String join(@Valid MemberRequestDto memberRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "home";
        }

        memberService.join(memberRequestDto);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult result, @RequestParam(defaultValue = "/") String redirectURL) {
        if (result.hasErrors()) {
            return "home";
        }

        if (memberService.login(loginForm.getMemberId(), loginForm.getMemberPw())) {
            userDetailsService.loadUserByUsername(loginForm.getMemberId()); //세션 생성
        } else {
            result.reject("loginFail", "The ID or password is not correct.");
            return "home";
        }

        if (redirectURL != null) {
            return "redirect:" + redirectURL;
        }

        return "redirect:/";
    }

    @GetMapping("/re-login")
    public String reLogin(@ModelAttribute LoginForm loginForm) {
        return "member/login";
    }

    @GetMapping("/update")
    public String update(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember,
                         @ModelAttribute UpdateForm updateForm, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        model.addAttribute("loginMember", loginMember);
        return "member/update";
    }

    @PostMapping("/update")
    public String update(@Valid UpdateForm updateForm, BindingResult result) {
        if (result.hasErrors()) {
            return "member/update";
        }

        MemberResponseDto member = memberService.findMemberDto(updateForm.getId());
        memberService.update(member.getId(), updateForm.getOriginPw(), updateForm.getNewPw(), updateForm.getName(), updateForm.getAge());

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("review", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/withdraw")
    public String withdraw(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        return "member/withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember, @RequestParam String pw, HttpServletRequest request) {
        if (!StringUtils.hasLength(pw) || StringUtils.containsWhitespace(pw)) {
            return "member/withdraw";
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        memberService.withdraw(loginMember.getId(), pw);

        return "redirect:/";
    }

    @PostConstruct
    public void init() throws Exception {
        for (int i = 1; i <= 5; i++) {
            MemberRequestDto dto = new MemberRequestDto();
            dto.setMemberId("qkrdbsgh" + i);
            dto.setMemberPw("qkrdbsgh" + i);
            dto.setName("yunho" + i);
            dto.setAge(i);

            memberService.join(dto);
            Thread.sleep(5);
        }

        MemberRequestDto dto = new MemberRequestDto();
        dto.setMemberId("qwe");
        dto.setMemberPw("qwe");
        dto.setName("ADMIN");
        dto.setAge(100);
        dto.setAuth(Role.ADMIN.getRole());
        memberService.join(dto);
    }
}
