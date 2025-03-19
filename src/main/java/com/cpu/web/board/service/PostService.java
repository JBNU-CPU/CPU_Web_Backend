package com.cpu.web.board.service;

import com.cpu.web.board.dto.request.PostRequestDTO;
import com.cpu.web.board.dto.response.PostResponseDTO;
import com.cpu.web.board.dto.response.SearchResponseDTO;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.repository.PostRepository;
import com.cpu.web.exception.CustomException;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 글 생성
    public Post createPost(@Valid PostRequestDTO postRequestDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        Post post = postRequestDTO.toPostEntity(member);
        return postRepository.save(post);
    }

    // 페이징 처리된 전체 글 조회
    public Page<PostResponseDTO> getAllPosts(int page, int size) {
        Page<Post> posts = postRepository.findAll(PageRequest.of(page, size, Sort.by("postId").descending()));
        return posts.map(PostResponseDTO::new);
    }

    // 특정 글 조회
    public Optional<PostResponseDTO> getPostById(Long id) {
        return postRepository.findById(id).map(PostResponseDTO::new);
    }

    // 글 수정
    public PostResponseDTO updatePost(Long id, @Valid PostRequestDTO postRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.UNAUTHORIZED));

        if (!username.equals(postRepository.findById(id).orElseThrow().getMember().getUsername())) {
            throw new CustomException("수정 권한이 없는 유저입니다: " + username, HttpStatus.FORBIDDEN);
        }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException("게시글이 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));

        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        Post updatedPost = postRepository.save(post);
        return new PostResponseDTO(updatedPost);
    }

    // 글 삭제
    public void deletePost(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.UNAUTHORIZED));

        // 해당 게시글이 존재하는지 확인
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException("게시글이 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));

        // 관리자이거나 게시글 작성자인 경우 삭제 가능
        if (isAdmin || username.equals(post.getMember().getUsername())) {
            postRepository.deleteById(id);
        } else {
            throw new CustomException("삭제 권한이 없는 유저입니다: " + username, HttpStatus.FORBIDDEN);
        }
    }

    public List<SearchResponseDTO> searchByTitle(String title) {

        return postRepository.findPostTitle(title);
    }
}
