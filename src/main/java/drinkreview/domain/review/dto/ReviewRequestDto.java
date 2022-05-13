package drinkreview.domain.review.dto;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ReviewRequestDto {

    @NotEmpty private Member member;
    @NotEmpty private Drink drink;
    @NotEmpty private String title;
    @NotEmpty private String content;
    @NotEmpty private double score;

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
