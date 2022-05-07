package drinkreview.domain.comment.repository;

import drinkreview.domain.comment.dto.CommentQueryDto;

import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentQueryDto> searchList();
}
