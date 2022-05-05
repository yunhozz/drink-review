package drinkreview.domain.review.repository;

import drinkreview.domain.review.dto.ReviewQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {

    Boolean exist(Long id);
    List<ReviewQueryDto> searchList();
    Page<ReviewQueryDto> searchPageByScoreOrder(Pageable pageable); //평점순
    Page<ReviewQueryDto> searchPageByDateOrder(Pageable pageable); //최신순
    Page<ReviewQueryDto> searchPageByViewOrder(Pageable pageable); //인기순
    Page<ReviewQueryDto> searchPageByKeyword(String keyword, Pageable pageable); //검색 기능 + 페이징
}
