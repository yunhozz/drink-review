package drinkreview.domain.review.dto;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ReviewRequestDto {

    private Member member;
    private Drink drink;
    @NotBlank private String title;
    @NotBlank private String content;
    private double score;

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
