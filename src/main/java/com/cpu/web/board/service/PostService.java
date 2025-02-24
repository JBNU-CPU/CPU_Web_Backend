package com.cpu.web.board.service;

import com.cpu.web.board.dto.request.PostRequestDTO;
import com.cpu.web.board.dto.response.PostResponseDTO;
import com.cpu.web.board.dto.response.SearchResponseDTO;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.repository.PostRepository;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Post createPost(PostRequestDTO postRequestDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("현재 로그인한 사용자 (글 신청)" + username);
        Optional<Member> member = memberRepository.findByUsername(username);
        String title = postRequestDTO.getTitle();
        String content = postRequestDTO.getContent();

        // 제목 유효한지
        if (title == null) {
            throw new IllegalArgumentException("제목이 유효하지 않습니다.");
        } else if (title.isEmpty()) {
            throw new IllegalArgumentException("제목이 유효하지 않습니다.");
        } else if (title.isBlank()) {
            throw new IllegalArgumentException("제목이 유효하지 않습니다.");
        }

        // 내용 유효한지
        if (content == null) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isEmpty()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isBlank()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        }

        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Post post = postRequestDTO.toPostEntity(member.get());
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
    public PostResponseDTO updatePost(Long id, PostResponseDTO postResponseDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다: " + username);
        }

        if (!username.equals(postRepository.findById(id).orElseThrow().getMember().getUsername())) {
            throw new IllegalArgumentException("수정 권한이 없는 유저입니다: " + username);
        }

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + id));

        post.setTitle(postResponseDTO.getTitle());
        post.setContent(postResponseDTO.getContent());
        Post updatedPost = postRepository.save(post);
        return new PostResponseDTO(updatedPost);
    }

    // 글 삭제
    public void deletePost(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다: " + username);
        }

        // 해당 게시글이 존재하는지 확인
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invalid Post ID: " + id));

        // 관리자이거나 게시글 작성자인 경우 삭제 가능
        if (isAdmin || username.equals(post.getMember().getUsername())) {
            postRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("삭제 권한이 없는 유저입니다: " + username);
        }
    }

    public List<SearchResponseDTO> searchByTitle(String title) {

        return postRepository.findPostTitle(title);
    }
}
