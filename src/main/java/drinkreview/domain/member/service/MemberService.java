package drinkreview.domain.member.service;

import drinkreview.domain.comment.Comment;
import drinkreview.domain.comment.repository.CommentRepository;
import drinkreview.domain.member.Member;
import drinkreview.domain.member.dto.MemberRequestDto;
import drinkreview.domain.member.dto.MemberResponseDto;
import drinkreview.domain.member.repository.MemberRepository;
import drinkreview.domain.order.Order;
import drinkreview.domain.order.repository.OrderRepository;
import drinkreview.domain.review.Review;
import drinkreview.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final OrderRepository orderRepository;

    //회원 가입
    public Long join(MemberRequestDto dto) {
        validateDuplicated(dto.getMemberId()); //아이디 중복 검사

        dto.setAuth("USER");
        Member member = memberRepository.save(dto.toEntity());

        return member.getId();
    }

    //로그인
    public MemberResponseDto login(String memberId, String memberPw) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalStateException("Please insert ID again."));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        //비밀번호 일치 여부 확인
        if (!encoder.matches(memberPw, member.getMemberPw())) {
            throw new IllegalStateException("The password is different. Please enter it again.");
        }

        return new MemberResponseDto(member);
    }

    //회원 탈퇴
    public void withdraw(Long id, String memberPw) {
        Member member = this.findMember(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        //비밀번호 일치 여부 확인
        if (!encoder.matches(memberPw, member.getMemberPw())) {
            throw new IllegalStateException("The password is different. Please enter it again.");
        }

        //주문 상태인지 확인
        if (memberRepository.isOrdering(member.getId())) {
            throw new IllegalStateException("This member has at least one order now : " + member.getId());
        }

        List<Review> reviews = reviewRepository.findWithUserId(id);
        List<Comment> comments = commentRepository.findWithUserId(id);
        List<Order> orders = orderRepository.findWithUserId(id);

        reviews.forEach(Review::deleteMember);
        comments.forEach(Comment::deleteMember);
        orders.forEach(Order::deleteMember);

        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMemberDto(Long id) {
        Member member = this.findMember(id);
        return new MemberResponseDto(member);
    }

    @Transactional(readOnly = true)
    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Member is null."));
    }

    @Transactional(readOnly = true)
    private List<Member> findMembers() {
        return memberRepository.findAll();
    }

    private void validateDuplicated(String memberId) {
        if (this.findMembers().stream().anyMatch(m -> m.getMemberId().equals(memberId))) {
            throw new IllegalStateException("There is duplicated member ID.");
        }
    }
}
