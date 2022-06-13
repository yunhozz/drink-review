package drinkreview.global.controller;

import drinkreview.domain.member.dto.MemberSessionResponseDto;
import drinkreview.domain.review.controller.OrderSelect;
import drinkreview.domain.review.controller.SearchForm;
import drinkreview.domain.review.dto.ReviewQueryDto;
import drinkreview.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final ReviewRepository reviewRepository;

    @GetMapping
    public String community(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember,
                            @ModelAttribute SearchForm searchForm, @PageableDefault(size = 10) Pageable pageable, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }
        model.addAttribute("loginMember", loginMember);

        Page<ReviewQueryDto> reviews = reviewRepository.searchPageByDateOrder(pageable);
        model.addAttribute("reviews", reviews);

        return "community";
    }

    @GetMapping("/search")
    public String search(@SessionAttribute(value = SessionConstant.LOGIN_MEMBER, required = false) MemberSessionResponseDto loginMember,
                         @ModelAttribute SearchForm searchForm, @RequestParam String keyword, @RequestParam OrderSelect orderSelect,
                         @PageableDefault(size = 10) Pageable pageable, Model model) {
        if (loginMember == null) {
            return "redirect:/member/re-login";
        }
        model.addAttribute("loginMember", loginMember);

        Page<ReviewQueryDto> reviews = Page.empty();
        if (keyword.isEmpty()) {
            reviews = reviewRepository.searchPageByDateOrder(pageable);
        } else {
            if (orderSelect.equals(OrderSelect.DATE_ORDER)) {
                reviews = reviewRepository.searchPageDateByKeyword(keyword, pageable);
            } else if (orderSelect.equals(OrderSelect.ACCURACY_ORDER)){
                reviews = reviewRepository.searchPageAccuracyByKeyword(keyword, pageable);
            }
        }
        model.addAttribute("reviews", reviews);

        return "community";
    }
}
