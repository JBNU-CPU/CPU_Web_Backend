package com.cpu.web.service.board;

import com.cpu.web.dto.board.BulletinDTO;
import com.cpu.web.dto.comment.BulletinCommentDTO;
import com.cpu.web.entity.board.Bulletin;
import com.cpu.web.entity.comment.BulletinComment;
import com.cpu.web.repository.comment.BulletinCommentRepository;
import com.cpu.web.repository.board.BulletinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BulletinService {

    private final BulletinRepository bulletinRepository;

    private final BulletinCommentRepository bulletinCommentRepository;

    // 글 생성
    public Bulletin createBulletin(BulletinDTO bulletinDTO) {

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
        return bulletinRepository.save(bulletin);
    }

    // 전체 글 조회
    public List<BulletinDTO> getAllBulletins() {
        return bulletinRepository.findAll().stream().map(BulletinDTO::new).collect(Collectors.toList());
    }

    // 특정 글 조회
    public Optional<BulletinDTO> getBulletinById(Long id) {
        return bulletinRepository.findById(id).map(BulletinDTO::new);
    }

    // 글 수정
    public BulletinDTO updateBulletin(Long id, BulletinDTO bulletinDTO) {
        Bulletin bulletin = bulletinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bulletin ID: " + id));
        bulletin.setTitle(bulletinDTO.getTitle());
        bulletin.setContent(bulletinDTO.getContent());
        bulletin.setAnonymous(bulletinDTO.isAnonymous());
        Bulletin updatedBulletin = bulletinRepository.save(bulletin);
        return new BulletinDTO(updatedBulletin);
    }

    // 글 삭제
    public void deleteBulletin(Long id) {
        if (!bulletinRepository.existsById(id)){
            throw new IllegalArgumentException("Invalid bulletin ID: " + id);
        }
        bulletinRepository.deleteById(id);
    }


    // 댓글 생성
    public BulletinComment createBulletinComment(BulletinCommentDTO bulletinCommentDTO) {
        String content = bulletinCommentDTO.getContent();
        Long id = bulletinCommentDTO.getBulletinId();

        // 내용 유효한지
        if (content == null) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isEmpty()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        } else if (content.isBlank()) {
            throw new IllegalArgumentException("내용이 유효하지 않습니다.");
        }

        Bulletin bulletin = bulletinRepository.findById(bulletinCommentDTO.getBulletinId()).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.: " + id));
        BulletinComment bulletinComment = bulletinCommentDTO.toBulletinCommentEntity(content, bulletin);
        return bulletinCommentRepository.save(bulletinComment);
    }

    // 특정 글 모든 댓글 조회
    public List<BulletinCommentDTO> getAllBulletinComments(Long id) {
        if (!bulletinRepository.existsById(id)){
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id);
        }
        return bulletinCommentRepository.findByBulletin_BulletinId(id).stream().map(BulletinCommentDTO::new).collect(Collectors.toList());
    }

    // 특정 댓글 조회
    public Optional<BulletinCommentDTO> getBulletinComment(Long id) {
        return bulletinCommentRepository.findById(id).map(BulletinCommentDTO::new);
    }

    // 댓글 수정
    public BulletinCommentDTO updateBulletinComment(Long id, BulletinCommentDTO bulletinCommentDTO) {
        BulletinComment bulletinComment = bulletinCommentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다: " + id));
        bulletinComment.setContents(bulletinCommentDTO.getContent());
        bulletinComment = bulletinCommentRepository.save(bulletinComment);
        return new BulletinCommentDTO(bulletinComment);
    }

    // 댓글 삭제
    public void deleteBulletinComment(Long id) {
        if (!bulletinCommentRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다: " + id);
        }
        bulletinCommentRepository.deleteById(id);
    }
}
