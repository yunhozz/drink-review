package drinkreview.domain.member.controller;

import drinkreview.domain.member.MemberService;
import drinkreview.domain.member.UserDetailsServiceImpl;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.global.enums.Role;
import drinkreview.global.ui.LoginMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/join")
    public String join(@Valid MemberRequestDto memberRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                log.info("errorName={}", error.toString());
            }
            return "home";
        }

        memberService.join(memberRequestDto);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm loginForm, BindingResult result, @RequestParam(defaultValue = "/") String redirectURL) {
        //에러 검증
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError error : errors) {
                log.info("errorName={}", error.toString());
            }
            return "home";
        } else {
            try {
                boolean isMember = memberService.login(loginForm.getMemberId(), loginForm.getMemberPw());
                if (!isMember) {
                    result.addError(new ObjectError("loginFail", "Passwords do not match. Insert again."));
                }
            } catch (Exception e) {
                result.addError(new ObjectError("loginFail", e.getMessage()));
            }

            if (result.hasErrors()) {
                List<ObjectError> errors = result.getAllErrors();
                for (ObjectError error : errors) {
                    log.info("errorName={}", error.toString());
                }
                return "home";
            }
            userDetailsService.loadUserByUsername(loginForm.getMemberId()); //회원 세션 저장

            if (redirectURL != null) {
                return "redirect:" + redirectURL;
            }
            return "redirect:/";
        }
    }

    @GetMapping("/re-login")
    public String reLogin(@ModelAttribute LoginForm loginForm) {
        return "member/login";
    }

    @GetMapping("/update")
    public String update(@LoginMember MemberSessionResponseDto loginMember, @ModelAttribute UpdateForm updateForm, Model model) {
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

        memberService.update(updateForm.getId(), updateForm.getOriginPw(), updateForm.getNewPw(), updateForm.getName(), updateForm.getAge());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return "redirect:/";
    }

    @GetMapping("/withdraw")
    public String withdraw(@LoginMember MemberSessionResponseDto loginMember) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }

        return "member/withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@LoginMember MemberSessionResponseDto loginMember, @RequestParam String pw, HttpServletRequest request) {
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
