package drinkreview.domain.review.dto;

import drinkreview.domain.drink.Drink;
import drinkreview.domain.member.Member;
import drinkreview.domain.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

    private Member member;
    private Drink drink;
    private String title;
    private String content;
    private double score;

    @Builder
    private ReviewRequestDto(Member member, Drink drink, String title, String content, double score) {
        this.member = member;
        this.drink = drink;
        this.title = title;
        this.content = content;
        this.score = score;
    }

    public Review toEntity() {
        return Review.builder()
                .member(member)
                .drink(drink)
                .title(title)
                .content(content)
                .writer(member.getName())
                .score(score)
                .view(0)
                .build();
    }
}
