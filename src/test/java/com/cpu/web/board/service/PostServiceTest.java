package com.cpu.web.board.service;

import com.cpu.web.board.InitTest;
import com.cpu.web.board.dto.request.PostRequestDTO;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.repository.PostRepository;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest extends InitTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostService postService;



    @Test
    public void 게시글_저장하기(){
        PostRequestDTO dto = new PostRequestDTO();
        dto.setContent("내용4");
        dto.setTitle("제목4");
        dto.setIsNotice(true);
        Member member = memberRepository.findById(publicMember.getMemberId()).orElseThrow();

        Post savePost = postRepository.save(dto.toPostEntity(member));

        Assertions.assertThat(savePost.getContent()).isEqualTo("내용4");
        Assertions.assertThat(savePost.getTitle()).isEqualTo("제목4");
    }

    @Test
    public void 게시글_읽기_조회수_증가(){
        Post post = postRepository.findById(publicPost.getPostId()).orElseThrow();
        int firstCount = post.getViewCount();

        postRepository.incrementViewCount(publicPost.getPostId());

        Post post1 = postRepository.findById(publicPost.getPostId()).orElseThrow();
        int secondCount = post1.getViewCount();

        Assertions.assertThat(firstCount+1).isEqualTo(secondCount);

    }

}