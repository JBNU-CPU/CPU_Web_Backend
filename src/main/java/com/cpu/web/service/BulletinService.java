package com.cpu.web.service;

import com.cpu.web.dto.BulletinDTO;
import com.cpu.web.entity.Bulletin;
import com.cpu.web.repository.BulletinRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BulletinService {

    private final BulletinRepository bulletinRepository;

    public BulletinService(BulletinRepository bulletinRepository){
        this.bulletinRepository = bulletinRepository;
    }

    // 글 저장
    public boolean createBulletin(BulletinDTO bulletinDTO){

        String title = bulletinDTO.getTitle();
        String content = bulletinDTO.getContent();

        // 제목 유효한지
        if(title == null){
            return false;
        }else if(title.isEmpty()){
            return false;
        }else if(title.isBlank()){
            return false;
        }

        // 내용 유효한지
        if(content == null){
            return false;
        }else if(content.isEmpty()){
            return false;
        }else if(content.isBlank()){
            return false;
        }

        Bulletin bulletin = bulletinDTO.toContentEntity();
        bulletinRepository.save(bulletin);

        return true;

    }
    
    // 전체 글 조회
    public List<BulletinDTO> getAllBulletin() {
        return bulletinRepository.findAll().stream()
                .map(BulletinDTO::new)
                .collect(Collectors.toList());
    }

    // 특정 글 조회
    public BulletinDTO getBulletinById(Long id) {
        Optional<Bulletin> bulletin = bulletinRepository.findById(id);
        return bulletin.map(BulletinDTO::new).orElse(null);
    }

    // 글 수정
    public void updateBulletin(Long id, BulletinDTO bulletinDTO) {
        Optional<Bulletin> existingBulletin = bulletinRepository.findById(id);
        if (existingBulletin.isPresent()){
            Bulletin bulletin = existingBulletin.get();
            bulletin.setTitle(bulletinDTO.getTitle());
            bulletin.setContent(bulletinDTO.getContent());
            bulletin.setAnonymous(bulletinDTO.isAnonymous());
            bulletinRepository.save(bulletin);
        } else {
            // 예외처리
        }
    }

    public void deleteBulletin(Long id) {
        bulletinRepository.deleteById(id);
    }

    // 글 삭제
    
}
