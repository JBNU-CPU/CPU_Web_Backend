package com.cpu.web.board.service;

import com.cpu.web.board.dto.request.CommentRequestDTO;
import com.cpu.web.board.dto.response.CommentResponseDTO;
import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.repository.CommentRepository;
import com.cpu.web.board.repository.PostRepository;
import com.cpu.web.exception.CustomException;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 댓글 작성
    public Comment createComment(CommentRequestDTO commentRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);
        String content = commentRequestDTO.getContent();
        Long id = commentRequestDTO.getPostId();


        Post post = postRepository.findById(commentRequestDTO.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.: " + id));
        Comment comment = commentRequestDTO.toCommentEntity(content, post, member.get());
        return commentRepository.save(comment);
    }
    
    // 특정 글 모든 댓글 조회
    public List<CommentResponseDTO> getAllComments(Long id) {
        if (!postRepository.existsById(id)){
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id);
        }
        return commentRepository.findByPost_PostId(id).stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }

    // 댓글 수정
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO commentRequestDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.FORBIDDEN));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다: " + id));

        // 댓글 작성자 또는 관리자인지 확인
        if (!username.equals(comment.getMember().getUsername())){
            throw new CustomException("수정 권한이 없는 유저입니다: " + username, HttpStatus.UNAUTHORIZED);
        }

        comment.setContent(commentRequestDTO.getContent());
        comment = commentRepository.save(comment);
        return new CommentResponseDTO(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("로그인한 사용자만 접근 가능합니다.", HttpStatus.UNAUTHORIZED));
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        System.out.println("isAdmin = " + isAdmin);
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CustomException("해당 댓글이 존재하지 않습니다: " + id, HttpStatus.FORBIDDEN));


        // 관리자이거나 댓글 작성자만 삭제 가능
        if (!(isAdmin || username.equals(comment.getMember().getUsername()))) {
            throw new CustomException("삭제 권한이 없는 유저입니다: " + username, HttpStatus.FORBIDDEN);
        }


        commentRepository.deleteById(id);
    }
}
