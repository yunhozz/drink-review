package drinkreview.domain.comment.service;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.comment.CommentChild;
import drinkreview.domain.comment.dto.CommentChildRequestDto;
import drinkreview.domain.comment.repository.CommentChildRepository;
import drinkreview.domain.comment.repository.CommentRepository;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.global.enums.DeleteStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentChildService {

    private final CommentChildRepository commentChildRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public Long makeCommentChild(CommentChildRequestDto dto, Long userId, Long commentId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("Comment is null."));

        dto.setMember(member);
        dto.setComment(comment);
        CommentChild commentChild = commentChildRepository.save(dto.toEntity());

        return commentChild.getId();
    }

    public void updateCommentChild(CommentChildRequestDto dto, Long commentChildId) {
        CommentChild commentChild = this.findCommentChild(commentChildId);

        //작성자만 수정 가능
        if (!commentChild.getMember().getId().equals(dto.getMember().getId())) {
            throw new IllegalStateException("You do not have permission.");
        }

        commentChild.updateContent(dto.getContent());
    }

    public void deleteCommentChild(Long commentChildId, Long commentId, Long userId) {
        CommentChild commentChild = this.findCommentChild(commentChildId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("Comment is null."));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));

        //댓글 작성자만 삭제 가능
        if (commentChild.getMember().getId().equals(member.getId())) {
            //댓글 삭제 상태 -> 대댓글 하나 ?
            if (comment.getIsDeleted() == DeleteStatus.Y) {
                if (commentChildRepository.isLastOne()) {
                    commentRepository.delete(comment); //cascade delete : CommentChild
                } else {
                    commentChildRepository.delete(commentChild);
                }
            }
        } else {
            throw new IllegalStateException("You do not have permission.");
        }
    }

    @Transactional(readOnly = true)
    private CommentChild findCommentChild(Long commentChildId) {
        return commentChildRepository.findById(commentChildId)
                .orElseThrow(() -> new IllegalStateException("CommentChild is null."));
    }
}
