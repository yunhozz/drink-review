package drinkreview.domain.comment.service;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.comment.dto.CommentRequestDto;
import drinkreview.domain.comment.dto.CommentResponseDto;
import drinkreview.domain.comment.repository.CommentRepository;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.domain.review.Review;
import drinkreview.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public Long makeComment(CommentRequestDto dto, Long userId, Long reviewId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Review is null."));

        dto.setMember(member);
        dto.setReview(review);
        Comment comment = commentRepository.save(dto.toEntity());

        return comment.getId();
    }

    public void updateComment(CommentRequestDto dto, Long commentId) {
        Comment comment = this.findComment(commentId);

        //작성자만 수정 가능
        if (!comment.getMember().getId().equals(dto.getMember().getId())) {
            throw new IllegalStateException("You do not have permission.");
        }

        comment.updateContent(dto.getContent());
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = this.findComment(commentId);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));

        //댓글 작성자만 삭제 가능
        if (comment.getMember().getId().equals(member.getId())) {
            //대댓글이 없으면 아예 삭제, 하나라도 있으면 내용만 변경
            if (comment.getCommentChildList().isEmpty()) {
                commentRepository.delete(comment);
            } else {
                comment.delete();
            }
        } else {
            throw new IllegalStateException("You do not have permission.");
        }
    }

    @Transactional(readOnly = true)
    public CommentResponseDto findCommentDto(Long commentId) {
        Comment comment = this.findComment(commentId);
        return new CommentResponseDto(comment);
    }

    @Transactional(readOnly = true)
    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("Comment is null."));
    }
}
