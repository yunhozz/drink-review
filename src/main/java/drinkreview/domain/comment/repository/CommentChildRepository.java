package drinkreview.domain.comment.repository;

import drinkreview.domain.comment.CommentChild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentChildRepository extends JpaRepository<CommentChild, Long> {

    @Query("select cc from CommentChild cc join fetch cc.member m where m.id = :id")
    List<CommentChild> findWithUserId(@Param("id") Long userId);
}
