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
    public String community(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember,
                            @ModelAttribute SearchForm searchForm, @PageableDefault(size = 10) Pageable pageable, Model model) {
        if (loginMember == null) {
            return "member/login";
        }
        model.addAttribute("loginMember", loginMember);

        Page<ReviewQueryDto> reviews = reviewRepository.searchPageByDateOrder(pageable);
        model.addAttribute("reviews", reviews);

        return "community";
    }

    @PostMapping
    public String communitySearch(@SessionAttribute(SessionConstant.LOGIN_MEMBER) MemberSessionResponseDto loginMember,
                                  SearchForm searchForm, Pageable pageable, Model model) {
        if (loginMember == null) {
            return "member/login";
        }
        model.addAttribute("loginMember", loginMember);

        Page<ReviewQueryDto> reviews = Page.empty();
        if (searchForm.getKeyword().isEmpty()) {
            reviews = reviewRepository.searchPageByDateOrder(pageable);
        } else {
            if (searchForm.getOrderSelect().equals(OrderSelect.DATE_ORDER)) {
                reviews = reviewRepository.searchPageDateByKeyword(searchForm.getKeyword(), pageable);
            } else if (searchForm.getOrderSelect().equals(OrderSelect.ACCURACY_ORDER)){
                reviews = reviewRepository.searchPageAccuracyByKeyword(searchForm.getKeyword(), pageable);
            }
        }
        model.addAttribute("reviews", reviews);

        return "community";
    }
}