package drinkreview.domain.review.dto;

import drinkreview.domain.comment.dto.CommentResponseDto;
import drinkreview.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private Long userId;
    private Long drinkId;
    private String title;
    private String content;
    private String memberName;
    private double score;
    private int view;
    private List<CommentResponseDto> comments;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.userId = review.getMember().getId();
        this.drinkId = review.getDrink().getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.memberName = review.getMemberName();
        this.score = review.getScore();
        this.view = review.getView();
        this.comments = review.getComments().stream()
                .map(CommentResponseDto::new)
                .toList();
    }
}
