package drinkreview.domain.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import drinkreview.domain.member.MemberSearchCondition;
import drinkreview.domain.member.dto.MemberQueryDto;
import drinkreview.domain.member.dto.QMemberQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static drinkreview.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberQueryDto> searchByCondition(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberQueryDto(
                        member.memberId,
                        member.name,
                        member.age,
                        member.auth
                ))
                .from(member)
                .where(
                        memberIdEq(condition.getMemberId()),
                        memberNameEq(condition.getName()),
                        memberAgeGoe(condition.getAgeGoe()),
                        memberAgeLoe(condition.getAgeLoe()),
                        memberAuthEq(condition.getAuth())
                )
                .fetch();
    }

    @Override
    public Page<MemberQueryDto> searchByPage(Pageable pageable) {
        List<MemberQueryDto> content = queryFactory
                .select(new QMemberQueryDto(
                        member.memberId,
                        member.name,
                        member.age,
                        member.auth
                ))
                .from(member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public Page<MemberQueryDto> searchByConditionPage(MemberSearchCondition condition, Pageable pageable) {
        List<MemberQueryDto> content = queryFactory
                .select(new QMemberQueryDto(
                        member.memberId,
                        member.name,
                        member.age,
                        member.auth
                ))
                .from(member)
                .where(
                        memberIdEq(condition.getMemberId()),
                        memberNameEq(condition.getName()),
                        memberAgeGoe(condition.getAgeGoe()),
                        memberAgeLoe(condition.getAgeLoe()),
                        memberAuthEq(condition.getAuth())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    private BooleanExpression memberIdEq(String memberId) {
        return StringUtils.hasText(memberId) ? member.memberId.eq(memberId) : null;
    }

    private BooleanExpression memberNameEq(String name) {
        return StringUtils.hasText(name) ? member.name.eq(name) : null;
    }

    private BooleanExpression memberAgeGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }

    private BooleanExpression memberAgeLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

    private BooleanExpression memberAuthEq(String auth) {
        return StringUtils.hasText(auth) ? member.auth.eq(auth) : null;
    }
}
