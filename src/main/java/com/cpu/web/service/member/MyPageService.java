package com.cpu.web.service.member;

import com.cpu.web.dto.member.MyPageDTO;
import com.cpu.web.entity.member.Member;
import com.cpu.web.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 마이페이지 유저 정보 조회
    public MyPageDTO getUserProfile() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        return memberRepository.findByUsername(username)
                .map(MyPageDTO::new)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // 마이페이지 유저 정보 수정
    public MyPageDTO updateProfile(MyPageDTO myPageDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        member.setPersonName(myPageDTO.getPersonName());
        // 비밀번호나 역할은 관리자만 수정할 수 있다고 가정하고, 여기서는 수정하지 않음.

        memberRepository.save(member);
        return new MyPageDTO(member);
    }

    // 회원 탈퇴 처리
    public boolean withdrawl(String password) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, member.getPassword())) {
            memberRepository.delete(member);
            return true;
        } else {
            return false;
        }
    }
}
