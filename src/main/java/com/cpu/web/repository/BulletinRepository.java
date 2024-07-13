package com.cpu.web.repository;

import com.cpu.web.entity.Bulletin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BulletinRepository extends JpaRepository<Bulletin, Integer> {
}
