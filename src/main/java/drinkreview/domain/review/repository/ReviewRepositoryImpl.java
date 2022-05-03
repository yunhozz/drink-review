package drinkreview.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import drinkreview.domain.review.dto.QReviewQueryDto;
import drinkreview.domain.review.dto.ReviewQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static drinkreview.domain.drink.QDrink.drink;
import static drinkreview.domain.member.QMember.member;
import static drinkreview.domain.review.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean exist(Long id) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(review)
                .where(review.id.eq(id))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public List<ReviewQueryDto> searchList() {
        return queryFactory
                .select(new QReviewQueryDto(
                        review.id, review.title, review.content, review.score,
                        member.id, member.memberId, member.name,
                        drink.id, drink.name
                ))
                .from(review)
                .join(review.member, member)
                .join(review.drink, drink)
                .orderBy(review.lastModifiedDate.desc())
                .fetch();
    }

    @Override
    public Page<ReviewQueryDto> searchPageByScoreOrder(Pageable pageable) {
        List<ReviewQueryDto> content = queryFactory
                .select(new QReviewQueryDto(
                        review.id, review.title, review.content, review.score,
                        member.memberId, member.name,
                        drink.name
                ))
                .from(review)
                .join(review.member, member)
                .join(review.drink, drink)
                .orderBy(review.score.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public Page<ReviewQueryDto> searchPageByDateOrder(Pageable pageable) {
        List<ReviewQueryDto> content = queryFactory
                .select(new QReviewQueryDto(
                        review.id, review.title, review.content, review.score,
                        member.memberId, member.name,
                        drink.name
                ))
                .from(review)
                .join(review.member, member)
                .join(review.drink, drink)
                .orderBy(review.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }
}
