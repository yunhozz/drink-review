package drinkreview.domain.member.repository;

import drinkreview.domain.member.MemberSearchCondition;
import drinkreview.domain.member.dto.MemberQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberRepositoryCustom {

    Boolean exist(Long id);
    List<MemberQueryDto> searchByCondition(MemberSearchCondition condition);
    Page<MemberQueryDto> searchByPage(Pageable pageable);
    Page<MemberQueryDto> searchByConditionPage(MemberSearchCondition condition, Pageable pageable);
}
