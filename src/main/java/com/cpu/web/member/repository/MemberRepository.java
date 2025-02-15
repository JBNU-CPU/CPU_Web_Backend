package com.cpu.web.member.repository;

import com.cpu.web.member.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // ID (username) 중복 체크 및 검색
    boolean existsByUsername(String username);  // 이미 존재하는 아이디인지 확인
    Optional<Member> findByUsername(String username);

    // 닉네임 중복 체크
    boolean existsByNickName(String nickName);  // 이미 존재하는 닉네임인지 확인

    // 이메일 중복 체크
    boolean existsByEmail(String email);        // 이미 존재하는 이메일인지 확인

    // 특정 ROLE 조회
    List<Member> findByRole(Member.Role role);
    
    @Transactional
    void deleteByUsername(String username);
}
