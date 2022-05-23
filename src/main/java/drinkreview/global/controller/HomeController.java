package drinkreview.global.controller;

import drinkreview.domain.member.controller.LoginForm;
import drinkreview.domain.member.controller.SessionConstant;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@ModelAttribute MemberRequestDto memberRequestDto, @ModelAttribute LoginForm loginForm,
                       @SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember, Model model) {
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("loginMember", loginMember);
        return "loginHome";
    }
}
