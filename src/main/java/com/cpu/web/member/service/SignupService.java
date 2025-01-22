package com.cpu.web.member.service;

import com.cpu.web.member.dto.SignupDTO;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.entity.Member.Role;
import com.cpu.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long signup(SignupDTO signupDTO) {

        // ID 중복 여부 체크
        if (memberRepository.existsByUsername(signupDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 닉네임 중복 여부 체크
        if (memberRepository.existsByNickName(signupDTO.getNickName())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // 이메일 중복 여부 체크
        if (memberRepository.existsByEmail(signupDTO.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 중복 검사를 통과하면 회원가입 진행
        Member member = new Member();
        member.setUsername(signupDTO.getUsername());
        member.setPassword(bCryptPasswordEncoder.encode(signupDTO.getPassword()));
        member.setPersonName(signupDTO.getPersonName());
        member.setNickName(signupDTO.getNickName());
        member.setEmail(signupDTO.getEmail());

        // 기본적으로 ROLE_GUEST 설정
        member.setRole(Role.ROLE_MEMBER);

        // 회원 정보 저장
        member = memberRepository.save(member);
        return member.getMemberId(); // 사용자 ID 반환
    }

}
