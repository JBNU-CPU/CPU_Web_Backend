package com.cpu.web.board.service;

import com.cpu.web.board.dto.response.CommentDTO;
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
    public Comment createComment(CommentDTO commentDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);
        String content = commentDTO.getContent();
        Long id = commentDTO.getPostId();

        // 내용 유효한지
        if (content == null) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isEmpty()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isBlank()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        }

        if (member.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }

        Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.: " + id));
        Comment comment = commentDTO.toCommentEntity(content, post, member.get());
        return commentRepository.save(comment);
    }
    
    // 특정 글 모든 댓글 조회
    public List<CommentDTO> getAllComments(Long id) {
        if (!postRepository.existsById(id)){
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id);
        }
        return commentRepository.findByPost_PostId(id).stream().map(CommentDTO::new).collect(Collectors.toList());
    }

    // 댓글 수정
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 유저입니다: " + username);
        }

        if (!member.get().equals(memberRepository.findById(id))){
            throw new IllegalArgumentException("수정 권한이 없는 유저입니다: " + username);
        }

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다: " + id));
        comment.setContent(commentDTO.getContent());
        comment = commentRepository.save(comment);
        return new CommentDTO(comment);
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
