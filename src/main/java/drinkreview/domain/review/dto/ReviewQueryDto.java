package drinkreview.domain.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewQueryDto {

    //Review
    private Long reviewId;
    private String title;
    private String content;
    private double score;

    //Member
    private Long userId;
    private String memberId;
    private String memberName;

    //Drink
    private Long drinkId;
    private String drinkName;

    @QueryProjection
    public ReviewQueryDto(Long reviewId, String title, String content, double score, String memberId, String memberName, String drinkName) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.score = score;
        this.memberId = memberId;
        this.memberName = memberName;
        this.drinkName = drinkName;
    }

    @QueryProjection
    public ReviewQueryDto(Long reviewId, String title, String content, double score, Long userId, String memberId, String memberName, Long drinkId, String drinkName) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.score = score;
        this.userId = userId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.drinkId = drinkId;
        this.drinkName = drinkName;
    }
}
