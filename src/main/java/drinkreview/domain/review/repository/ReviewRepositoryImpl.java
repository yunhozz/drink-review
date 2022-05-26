package drinkreview.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import drinkreview.domain.comment.dto.CommentQueryDto;
import drinkreview.domain.comment.dto.QCommentQueryDto;
import drinkreview.domain.review.dto.QReviewQueryDto;
import drinkreview.domain.review.dto.ReviewQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

import static drinkreview.domain.comment.QComment.comment;
import static drinkreview.domain.drink.QDrink.drink;
import static drinkreview.domain.member.QMember.member;
import static drinkreview.domain.review.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean exist(Long reviewId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(review)
                .where(review.id.eq(reviewId))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public ReviewQueryDto selectReview(Long reviewId) {
        ReviewQueryDto findReview = queryFactory
                .select(new QReviewQueryDto(
                        review.id,
                        review.title,
                        review.content,
                        review.score,
                        review.view,
                        review.createdDate,
                        member.id,
                        member.memberId,
                        member.name,
                        drink.id,
                        drink.name
                ))
                .from(review)
                .join(review.member, member)
                .join(review.drink, drink)
                .where(review.id.eq(reviewId))
                .fetchOne();

        if (findReview == null) {
            throw new IllegalStateException("Can't find this review : " + reviewId);
        }

        List<CommentQueryDto> comments = queryFactory
                .select(new QCommentQueryDto(
                        comment.id,
                        comment.content,
                        comment.createdDate,
                        member.id,
                        member.memberId,
                        member.name,
                        comment.review.id
                ))
                .from(comment)
                .join(comment.member, member)
                .where(comment.review.id.eq(findReview.getReviewId()))
                .fetch();

        findReview.setComments(comments);

        return findReview;
    }

    @Override
    public List<ReviewQueryDto> findReviewList() {
        List<ReviewQueryDto> reviews = queryFactory
                .select(new QReviewQueryDto(
                        review.id,
                        review.title,
                        review.content,
                        review.score,
                        review.view,
                        review.createdDate,
                        member.id,
                        member.memberId,
                        member.name,
                        drink.id,
                        drink.name
                ))
                .from(review)
                .join(review.member, member)
                .join(review.drink, drink)
                .fetch();

        List<Long> reviewIds = reviews.stream()
                .map(ReviewQueryDto::getReviewId)
                .toList();

        List<CommentQueryDto> comments = queryFactory
                .select(new QCommentQueryDto(
                        comment.id,
                        comment.content,
                        comment.createdDate,
                        member.id,
                        member.memberId,
                        member.name,
                        comment.review.id
                ))
                .from(comment)
                .join(comment.member, member)
                .where(comment.review.id.in(reviewIds))
                .fetch();

        //key: reviewId & value: List<CommentQueryDto>
        Map<Long, List<CommentQueryDto>> commentMap = comments.stream()
                .collect(Collectors.groupingBy(CommentQueryDto::getReviewId));

        reviews.forEach(reviewQueryDto -> reviewQueryDto.setComments(commentMap.get(reviewQueryDto.getReviewId())));

        return reviews;
    }

    @Override
    public List<ReviewQueryDto> searchList() {
        return queryFactory
                .select(new QReviewQueryDto(
                        review.id,
                        review.title,
                        review.memberName,
                        review.score,
                        review.createdDate,
                        drink.id,
                        drink.name
                ))
                .from(review)
                .join(review.member, member)
                .join(review.drink, drink)
                .orderBy(review.createdDate.desc())
                .fetch();
    }

    @Override
    public Page<ReviewQueryDto> searchPageByScoreOrder(Pageable pageable) {
        List<ReviewQueryDto> content = queryFactory
                .select(new QReviewQueryDto(
                        review.id,
                        review.title,
                        review.memberName,
                        review.score,
                        review.createdDate,
                        drink.id,
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
                        review.id,
                        review.title,
                        review.memberName,
                        review.score,
                        review.createdDate,
                        drink.id,
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

    @Override
    public Page<ReviewQueryDto> searchPageByViewOrder(Pageable pageable) {
        List<ReviewQueryDto> content = queryFactory
                .select(new QReviewQueryDto(
                        review.id,
                        review.title,
                        review.memberName,
                        review.score,
                        review.createdDate,
                        drink.id,
                        drink.name
                ))
                .from(review)
                .join(review.member, member)
                .join(review.drink, drink)
                .orderBy(review.view.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(review.count())
                .from(review)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<ReviewQueryDto> searchPageDateByKeyword(String keyword, Pageable pageable) {
        List<ReviewQueryDto> content = queryFactory
                .select(new QReviewQueryDto(
                        review.id,
                        review.title,
                        review.memberName,
                        review.score,
                        review.createdDate,
                        drink.id,
                        drink.name
                ))
                .from(review)
                .join(review.member, member)
                .join(review.drink, drink)
                .where(review.title.contains(keyword)
                        .or(review.content.contains(keyword))
                )
                .orderBy(review.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(review.count())
                .from(review)
                .join(review.member, member).fetchJoin()
                .join(review.drink, drink).fetchJoin()
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<ReviewQueryDto> searchPageAccuracyByKeyword(String keyword, Pageable pageable) {
        List<ReviewQueryDto> reviews = queryFactory
                .select(new QReviewQueryDto(
                        review.id,
                        review.title,
                        review.memberName,
                        review.score,
                        review.createdDate,
                        drink.id,
                        drink.name
                ))
                .from(review)
                .join(review.member, member)
                .join(review.drink, drink)
                .where(review.title.contains(keyword)
                        .or(review.content.contains(keyword))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<ReviewQueryDto, Integer> countMap = new HashMap<>();
        for (ReviewQueryDto review : reviews) {
            int count1 = countKeyword(review.getTitle(), keyword);
            int count2 = countKeyword(review.getContent(), keyword);
            countMap.put(review, count1 + count2);
        }
        List<Map.Entry<ReviewQueryDto, Integer>> entries = new ArrayList<>(countMap.entrySet());
        Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue())); //내림차순 정렬

        List<ReviewQueryDto> sortedReviews = new ArrayList<>();
        for (Map.Entry<ReviewQueryDto, Integer> entry : entries) {
            sortedReviews.add(entry.getKey());
        }

        Long count = queryFactory
                .select(review.count())
                .from(review)
                .join(review.member, member).fetchJoin()
                .join(review.drink, drink).fetchJoin()
                .fetchOne();

        return new PageImpl<>(sortedReviews, pageable, count);
    }

    private int countKeyword(String str, String keyword) {
        return str.length() - str.replace(keyword, "").length();
    }
}
