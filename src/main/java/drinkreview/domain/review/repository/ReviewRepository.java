package drinkreview.domain.review.repository;

import drinkreview.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    @Query("select r from Review r where r.title like %:keyword% or r.content like %:keyword%")
    List<Review> findWithKeyword(@Param("keyword") String keyword); //검색 기능

    @Modifying(clearAutomatically = true)
    @Query("update Review r set r.view = r.view + 1 where r.id = :reviewId")
    int addView(@Param("reviewId") Long reviewId); //조회수 증가
}
