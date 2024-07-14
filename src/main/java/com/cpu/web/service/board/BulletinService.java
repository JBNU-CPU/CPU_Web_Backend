package com.cpu.web.service.board;

import com.cpu.web.dto.board.BulletinDTO;
import com.cpu.web.entity.board.Bulletin;
import com.cpu.web.repository.board.BulletinRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BulletinService {

    private final BulletinRepository bulletinRepository;

    public BulletinService(BulletinRepository bulletinRepository) {
        this.bulletinRepository = bulletinRepository;
    }

    // 글 저장
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


        Bulletin bulletin = bulletinDTO.toContentEntity();
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


}
