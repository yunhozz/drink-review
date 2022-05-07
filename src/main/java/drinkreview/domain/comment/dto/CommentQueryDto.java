package drinkreview.domain.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentQueryDto {

    //Comment
    private Long commentId;
    private Long parentId;
    private List<ChildResponseDto> child;
    private String content;
    private LocalDateTime createDate;

    //Member
    private Long userId;
    private String memberId;
    private String name;

    //Review
    private Long reviewId;

    @QueryProjection
    public CommentQueryDto(Long commentId, Long parentId, List<ChildResponseDto> child, String content, LocalDateTime createDate, Long userId, String memberId, String name, Long reviewId) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.child = child;
        this.content = content;
        this.createDate = createDate;
        this.userId = userId;
        this.memberId = memberId;
        this.name = name;
        this.reviewId = reviewId;
    }
}
