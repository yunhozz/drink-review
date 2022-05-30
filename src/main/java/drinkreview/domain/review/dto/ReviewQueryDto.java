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
    private Long id;
    private String title;
    private String content;
    private String memberName;
    private double score;
    private int view;
    private LocalDateTime createdDate;

    //Member
    private Long userId;
    private String memberId;
    private String name;

    //Drink
    private Long drinkId;
    private String drinkName;

    //Comment
    private List<CommentQueryDto> comments;

    //리스트
    @QueryProjection
    public ReviewQueryDto(Long id, String title, String content, String memberName, double score, int view, LocalDateTime createdDate,
                          Long drinkId, String drinkName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberName = memberName;
        this.score = score;
        this.view = view;
        this.createdDate = createdDate;
        this.drinkId = drinkId;
        this.drinkName = drinkName;
    }

    //세부 내용 + comments
    @QueryProjection
    public ReviewQueryDto(Long id, String title, String content, double score, int view, LocalDateTime createdDate, Long userId,
                          String memberId, String name, Long drinkId, String drinkName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.score = score;
        this.view = view;
        this.createdDate = createdDate;
        this.userId = userId;
        this.memberId = memberId;
        this.name = name;
        this.drinkId = drinkId;
        this.drinkName = drinkName;
    }
}
