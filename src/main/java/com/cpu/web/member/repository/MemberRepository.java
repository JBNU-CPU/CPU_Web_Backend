package com.cpu.web.member.repository;

import com.cpu.web.member.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 페이지네이션 전체 유저 조회
    Page<Member> findAll(Pageable pageable);

    // ID (username) 중복 체크 및 검색
    boolean existsByUsername(String username);  // 이미 존재하는 아이디인지 확인
    Optional<Member> findByUsername(String username);

    // 닉네임 중복 체크
    boolean existsByNickName(String nickName);  // 이미 존재하는 닉네임인지 확인

    // 이메일 중복 체크
    boolean existsByEmail(String email);        // 이미 존재하는 이메일인지 확인

    // 휴대폰 번호 중복 체크
    boolean existsByPhone(String phone);        // 이미 존재하는 전화번호인지 확인

    // 특정 ROLE 조회
    Page<Member> findByRole(Member.Role role, Pageable pageable);

    Optional<Member> findByEmail(String email);

    @Transactional
    void deleteByUsername(String username);
}
