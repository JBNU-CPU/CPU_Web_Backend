package com.cpu.web.service.board;

import com.cpu.web.dto.board.BulletinDTO;
import com.cpu.web.entity.board.Bulletin;
import com.cpu.web.repository.board.BulletinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BulletinService {

    private final BulletinRepository bulletinRepository;

    // 페이징된 전체 글 조회
    public Page<BulletinDTO> getAllBulletins(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bulletinRepository.findAll(pageable).map(this::convertToDTO);
    }

    // 특정 글 조회
    public Optional<BulletinDTO> getBulletinById(Long id) {
        return bulletinRepository.findById(id).map(this::convertToDTO);
    }

    // 글 생성
    public BulletinDTO createBulletin(BulletinDTO bulletinDTO) {
        Bulletin bulletin = bulletinDTO.toBulletinEntity();
        Bulletin savedBulletin = bulletinRepository.save(bulletin);
        return convertToDTO(savedBulletin);
    }

    // 글 수정
    public BulletinDTO updateBulletin(Long id, BulletinDTO bulletinDTO) {
        Bulletin bulletin = bulletinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid bulletin ID: " + id));
        bulletin.setTitle(bulletinDTO.getTitle());
        bulletin.setContent(bulletinDTO.getContent());
        Bulletin updatedBulletin = bulletinRepository.save(bulletin);
        return convertToDTO(updatedBulletin);
    }

    // 글 삭제
    public void deleteBulletin(Long id) {
        if (!bulletinRepository.existsById(id)){
            throw new IllegalArgumentException("Invalid bulletin ID: " + id);
        }
        bulletinRepository.deleteById(id);
    }

    private BulletinDTO convertToDTO(Bulletin bulletin) {
        return new BulletinDTO(bulletin);
    }
}
