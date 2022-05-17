package drinkreview.domain.comment.repository;

import drinkreview.domain.comment.CommentChild;
import drinkreview.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CommentChildRepositoryTest {

    @Autowired CommentChildRepository commentChildRepository;
    @Autowired EntityManager em;

    @Test
    void isLastOneTrue() throws Exception {
        //given
        Member member = createMember("qkrdbsgh", "111", "yunho");
        em.persist(member);

        CommentChild commentChild = new CommentChild(member, "content", member.getName());

        //when
        commentChildRepository.save(commentChild);
        boolean result = commentChildRepository.isLastOne();

        //then
        assertThat(result).isTrue();
    }

    @Test
    void isLastOneFalse() throws Exception {
        //given
        Member member = createMember("qkrdbsgh", "111", "yunho");
        em.persist(member);

        CommentChild commentChild1 = new CommentChild(member, "content1", member.getName());
        CommentChild commentChild2 = new CommentChild(member, "content2", member.getName());

        //when
        commentChildRepository.save(commentChild1);
        commentChildRepository.save(commentChild2);

        boolean result = commentChildRepository.isLastOne();

        //then
        assertThat(result).isFalse();
    }

    private Member createMember(String memberId, String memberPw, String name) {
        return Member.builder()
                .memberId(memberId)
                .memberPw(memberPw)
                .name(name)
                .build();
    }
}