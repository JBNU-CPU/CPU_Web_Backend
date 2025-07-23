package com.cpu.web.board;

import com.cpu.web.board.dto.request.PostRequestDTO;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.repository.PostRepository;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public abstract class InitTest {

    @Autowired protected MemberRepository memberRepository;
    @Autowired protected PostRepository postRepository;

    protected Member publicMember;
    protected Post publicPost;

    @BeforeEach
    public void init(){
        PostRequestDTO dto = new PostRequestDTO();
        dto.setContent("테스트 내용");
        dto.setTitle("테스트 제목");
        dto.setIsNotice(true);

        Member member = new Member();
        member.setEmail("ha@naver.com");
        member.setUsername("test");
        member.setPassword("test!");
        member.setNickName("testNickName");
        member.setRole(Member.Role.ROLE_MEMBER);
        member.setPersonName("testName");
        member.setPhone("010-1234-5678");

        publicMember = memberRepository.save(member);

        publicPost = postRepository.save(dto.toPostEntity(member));
    }
}
