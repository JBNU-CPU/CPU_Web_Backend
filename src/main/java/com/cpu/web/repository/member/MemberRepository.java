package com.cpu.web.repository.member;

import com.cpu.web.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUsername(String username);
    Optional<Member> findByUsername(String username);
}
