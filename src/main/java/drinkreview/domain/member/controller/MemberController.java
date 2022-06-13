package drinkreview.domain.member.controller;

import drinkreview.domain.member.MemberService;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.dto.MemberResponseDto;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.member.UserDetailsServiceImpl;
import drinkreview.global.controller.SessionConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

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
    }
}
