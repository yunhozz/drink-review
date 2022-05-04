package drinkreview.domain.comment;

import drinkreview.domain.comment.dto.CommentRequestDto;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.domain.review.Review;
import drinkreview.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void updateComment(CommentRequestDto dto, Long commentId, Long userId) {
        Comment comment = this.findComment(commentId);

        if (!comment.getMember().getId().equals(userId)) {
            throw new IllegalStateException("You do not have permission.");
        }

        comment.updateComments(dto.getComments());
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = this.findComment(commentId);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Member is null."));

        //댓글 작성자만 삭제 가능
        if (comment.getMember().getId().equals(member.getId())) {
            commentRepository.delete(comment);

        } else {
            throw new IllegalStateException("You do not have permission.");
        }
    }

    @Transactional(readOnly = true)
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("Comment is null."));
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentList() {
        return commentRepository.findAll();
    }
}
