package drinkreview.global.ui;

import drinkreview.domain.member.controller.LoginForm;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.dto.MemberSessionResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@LoginMember MemberSessionResponseDto loginMember, @ModelAttribute MemberRequestDto memberRequestDto, @ModelAttribute LoginForm loginForm,
                       Model model) {
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("loginMember", loginMember);
        return "loginHome";
    }
}
