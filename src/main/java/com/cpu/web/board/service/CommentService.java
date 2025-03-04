package com.cpu.web.board.service;

import com.cpu.web.board.dto.request.CommentRequestDTO;
import com.cpu.web.board.dto.response.CommentResponseDTO;
import com.cpu.web.board.entity.Comment;
import com.cpu.web.board.entity.Post;
import com.cpu.web.board.repository.CommentRepository;
import com.cpu.web.board.repository.PostRepository;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 유저입니다: " + username);
        }

        if (!member.get().equals(memberRepository.findById(id))){
            throw new IllegalArgumentException("수정 권한이 없는 유저입니다: " + username);
        }

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다: " + id));
        comment.setContent(commentRequestDTO.getContent());
        comment = commentRepository.save(comment);
        return new CommentResponseDTO(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 유저입니다: " + username);
        }

        if (!member.get().equals(memberRepository.findById(id))){
            throw new IllegalArgumentException("삭제 권한이 없는 유저입니다: " + username);
        }

        if(!commentRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다: " + id);
        }
        commentRepository.deleteById(id);
    }
}
