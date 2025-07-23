package com.cpu.web.board.controller;

import com.cpu.web.board.InitTest;
import com.cpu.web.board.dto.response.PostResponseDTO;
import com.cpu.web.board.service.PostService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostControllerTest extends InitTest {

    private static final Logger log = LoggerFactory.getLogger(PostControllerTest.class);
    @Autowired
    private PostService postService;


    @Test
    public void readOne(){
        Long postId = publicPost.getPostId();
        Long memberId = publicMember.getMemberId();
        PostResponseDTO dto = postService.getPostById(postId, memberId).get();

        log.info("title = {}", dto.getTitle());
        log.info("content = {}", dto.getContent());
        log.info("view count = {}", dto.getViewCont());
        log.info("memberId = {}", dto.getMemberId());
        log.info("nickName = {}", dto.getNickName());

        assertThat(dto.getTitle()).isEqualTo("테스트 제목");
        assertEquals("테스트 내용", dto.getContent());
        assertEquals(1,dto.getViewCont());
        assertEquals(1, dto.getMemberId());
        assertEquals("testNickName", dto.getNickName());
    }

}