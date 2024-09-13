package com.cpu.web.board.repository;

import com.cpu.web.board.entity.BulletinComment;
import com.cpu.web.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost_PostId(Long postId);

}
