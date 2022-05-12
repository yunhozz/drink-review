package drinkreview.domain.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import drinkreview.domain.comment.dto.CommentQueryDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewQueryDto {

    //Review
    private Long reviewId;
    private String title;
    private String content;
    private double score;
    private int view;
    private LocalDateTime createdDate;

    //Member
    private Long userId;
    private String memberId;
    private String memberName;

    //Drink
    private Long drinkId;
    private String drinkName;

    //Comment
    private List<CommentQueryDto> comments;

    //리스트
    @QueryProjection
    public ReviewQueryDto(Long reviewId, String title, double score, LocalDateTime createdDate, Long userId, String memberName, Long drinkId, String drinkName) {
        this.reviewId = reviewId;
        this.title = title;
        this.score = score;
        this.createdDate = createdDate;
        this.userId = userId;
        this.memberName = memberName;
        this.drinkId = drinkId;
        this.drinkName = drinkName;
    }

    //세부 내용 + comments
    @QueryProjection
    public ReviewQueryDto(Long reviewId, String title, String content, double score, int view, LocalDateTime createdDate, Long userId, String memberId, String memberName, Long drinkId, String drinkName) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.score = score;
        this.view = view;
        this.createdDate = createdDate;
        this.userId = userId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.drinkId = drinkId;
        this.drinkName = drinkName;
    }
}
