package drinkreview.domain.comment.repository;

import drinkreview.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @Query("select c from Comment c join fetch c.member m where m.id = :id")
    List<Comment> findWithUserId(@Param("id") Long userId);
}
