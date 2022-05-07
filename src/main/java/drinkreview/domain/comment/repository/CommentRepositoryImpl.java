package drinkreview.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import drinkreview.domain.comment.dto.CommentQueryDto;
import drinkreview.domain.comment.dto.QCommentQueryDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static drinkreview.domain.comment.QComment.comment;
import static drinkreview.domain.member.QMember.member;
import static drinkreview.domain.review.QReview.review;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentQueryDto> searchList() {
        return queryFactory
                .select(new QCommentQueryDto(
                        comment.id, comment.parent.id, comment.child, comment.content, comment.createdDate,
                        member.id, member.memberId, member.name,
                        review.id
                ))
                .from(comment)
                .join(comment.member, member)
                .join(comment.review, review)
                .orderBy(comment.createdDate.desc())
                .fetch();
    }
}
