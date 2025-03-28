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

    public Post createPost(@Valid PostRequestDTO postRequestDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));

        if (postRequestDTO.getIsNotice() && !isAdmin) {
            throw new CustomException("관리자만 공지 게시글을 작성할 수 있습니다.", HttpStatus.FORBIDDEN);
        }

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

    public PostResponseDTO updatePost(Long id, @Valid PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.UNAUTHORIZED));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException("게시글이 존재하지 않습니다: " + id, HttpStatus.NOT_FOUND));

        // 관리자가 아니고, 수정하려는 게시글이 공지글일 때 예외 처리
        if (post.getIsNotice() && !isAdmin) {
            throw new CustomException("관리자만 공지 게시글을 수정할 수 있습니다.", HttpStatus.FORBIDDEN);
        }

        if (!username.equals(post.getMember().getUsername()) && !isAdmin) {
            throw new CustomException("수정 권한이 없는 유저입니다: " + username, HttpStatus.FORBIDDEN);
        }

        // 기존 게시글의 isNotice 값을 가져와 새 게시글 데이터에 설정
        postRequestDTO.setIsNotice(post.getIsNotice());

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
