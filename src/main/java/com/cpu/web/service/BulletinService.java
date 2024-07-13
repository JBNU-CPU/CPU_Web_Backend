package com.cpu.web.service;

import com.cpu.web.dto.BulletinDTO;
import com.cpu.web.entity.Bulletin;
import com.cpu.web.repository.BulletinRepository;
import org.springframework.stereotype.Service;

@Service
public class BulletinService {

    private final BulletinRepository bulletinRepository;

    public BulletinService(BulletinRepository bulletinRepository){
        this.bulletinRepository = bulletinRepository;
    }

    public boolean createContent(BulletinDTO bulletinDTO){

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

}
