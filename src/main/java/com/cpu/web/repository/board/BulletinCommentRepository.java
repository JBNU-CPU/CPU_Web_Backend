package com.cpu.web.repository.board;

import com.cpu.web.entity.comment.BulletinComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BulletinCommentRepository extends JpaRepository<BulletinComment, Long> {

}
