package drinkreview.domain.comment.repository;

import drinkreview.domain.comment.dto.CommentQueryDto;
import drinkreview.domain.comment.dto.CommentResponseDto;

import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentResponseDto> searchListTest(Long reviewId);
    List<CommentQueryDto> searchList(Long reviewId);
}
