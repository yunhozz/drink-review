package drinkreview.domain.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import drinkreview.domain.comment.Comment;
import drinkreview.domain.comment.dto.ChildResponseDto;
import drinkreview.domain.comment.dto.CommentQueryDto;
import drinkreview.domain.comment.dto.CommentResponseDto;
import drinkreview.domain.comment.dto.QCommentQueryDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static drinkreview.domain.comment.QComment.comment;
import static drinkreview.domain.member.QMember.member;
import static drinkreview.domain.review.QReview.review;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponseDto> searchListTest(Long reviewId) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .join(comment.member).fetchJoin()
                .join(comment.review).fetchJoin()
                .leftJoin(comment.parent).fetchJoin()
                .where(review.id.eq(reviewId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createdDate.asc()
                )
                .fetch();

        for (Comment comment : comments) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }

        return commentResponseDtoList;
    }

    @Override
    public List<CommentQueryDto> searchList(Long reviewId) {
        queryFactory
                .select(new QCommentQueryDto(
                        comment.id,
                        comment.parent.id,
                        Projections.constructor(
                                ChildResponseDto.class,

                        )
                        comment.content,
                        comment.createdDate,
                        member.id,
                        member.memberId,
                        member.name,
                        review.id
                ))
                .join(comment.member, member)
                .join(comment.review, review)
                .join(comment.parent)
                .where(review.id.eq(reviewId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createdDate.asc()
                )
                .fetch();
    }
}
