package drinkreview.domain.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentQueryDto {

    //Comment
    private Long commentId;
    private String content;
    private LocalDateTime createDate;

    //Member
    private Long userId;
    private String memberId;
    private String name;

    //Review
    private Long reviewId;

    @QueryProjection
    public CommentQueryDto(Long commentId, String content, LocalDateTime createDate, Long userId, String memberId, String name, Long reviewId) {
        this.commentId = commentId;
        this.content = content;
        this.createDate = createDate;
        this.userId = userId;
        this.memberId = memberId;
        this.name = name;
        this.reviewId = reviewId;
    }
}
