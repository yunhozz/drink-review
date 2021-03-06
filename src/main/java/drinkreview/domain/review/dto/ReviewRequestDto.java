package drinkreview.domain.review.dto;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ReviewRequestDto {

    private Member member;
    private Drink drink;

    @NotBlank(message = "Please input title.")
    private String title;

    @NotBlank(message = "Please input content.")
    private String content;

    @NotNull(message = "Please choose score.")
    private Double score;

    public Review toEntity() {
        return Review.builder()
                .member(member)
                .drink(drink)
                .title(title)
                .content(content)
                .memberName(member.getName())
                .score(score)
                .view(0)
                .build();
    }
}
