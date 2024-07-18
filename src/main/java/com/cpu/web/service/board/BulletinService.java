package com.cpu.web.service.board;

import com.cpu.web.dto.board.BulletinDTO;
import com.cpu.web.dto.comment.BulletinCommentDTO;
import com.cpu.web.dto.comment.NotificationCommentDTO;
import com.cpu.web.entity.board.Bulletin;
import com.cpu.web.entity.comment.BulletinComment;
import com.cpu.web.repository.comment.BulletinCommentRepository;
import com.cpu.web.repository.board.BulletinRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BulletinService {

    private final BulletinRepository bulletinRepository;

    private final BulletinCommentRepository bulletinCommentRepository;

    public BulletinService(BulletinRepository bulletinRepository, BulletinCommentRepository bulletinCommentRepository) {

        this.bulletinRepository = bulletinRepository;
        this.bulletinCommentRepository = bulletinCommentRepository;
    }

    // 글 생성
    public void createBulletin(BulletinDTO bulletinDTO) {

        String title = bulletinDTO.getTitle();
        String content = bulletinDTO.getContent();

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


        Bulletin bulletin = bulletinDTO.toBulletinEntity();
        bulletinRepository.save(bulletin);
    }

    // 전체 글 조회
    public List<BulletinDTO> getAllBulletin() {
        return bulletinRepository.findAll().stream().map(BulletinDTO::new).collect(Collectors.toList());
    }

    // 특정 글 조회
    public BulletinDTO getBulletinById(Long id) {
        Optional<Bulletin> bulletin = bulletinRepository.findById(id);
        return bulletin.map(BulletinDTO::new).orElse(null);
    }

    // 글 수정
    public void updateBulletin(Long id, BulletinDTO bulletinDTO) {
        Bulletin bulletin = bulletinRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        bulletin.setTitle(bulletinDTO.getTitle());
        bulletin.setContent(bulletinDTO.getContent());
        bulletin.setAnonymous(bulletinDTO.isAnonymous());
        bulletinRepository.save(bulletin);
    }

    // 글 삭제
    public void deleteBulletin(Long id) {
        bulletinRepository.deleteById(id);
    }


    // 댓글 생성
    public void createBulletinComment(BulletinCommentDTO bulletinCommentDTO) {
        String content = bulletinCommentDTO.getContent();

        // 내용 유효한지
        if (content == null) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isEmpty()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isBlank()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        }

        Bulletin bulletin = bulletinRepository.findById(bulletinCommentDTO.getBulletinId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글에 접근하였습니다. 접근하신 게시글 아이디는 다음과 같습니다.: " + bulletinCommentDTO.getBulletinId()));
        BulletinComment bulletinComment = bulletinCommentDTO.toBulletinCommentEntity(bulletin);
        bulletinCommentRepository.save(bulletinComment);
    }
}
