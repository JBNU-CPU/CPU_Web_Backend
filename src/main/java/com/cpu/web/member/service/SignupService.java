package com.cpu.web.member.service;

import com.cpu.web.exception.CustomException;
import com.cpu.web.member.dto.response.SignupDTO;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.exception.DuplicateResourceException;
import com.cpu.web.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            throw new CustomException("중복된 ID 입니다", HttpStatus.BAD_REQUEST);
        }

        // ID 형식 체크
        if (!signupDTO.getUsername().matches("^20\\d{7}$")) {
            throw new CustomException("ID는 학번 형식이어야 합니다.", HttpStatus.BAD_REQUEST);
        }

        // 닉네임 중복 여부 체크
        if (memberRepository.existsByNickName(signupDTO.getNickName())) {
            throw new CustomException("중복된 닉네임입니다", HttpStatus.BAD_REQUEST);
        }

        // 이메일 중복 여부 체크
        if (memberRepository.existsByEmail(signupDTO.getEmail())) {
            throw new CustomException("중복된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        // 전화번호 중복 여부 체크
        if (memberRepository.existsByPhone(signupDTO.getPhone())) {
            throw new CustomException("중복된 전화번호입니다.", HttpStatus.BAD_REQUEST);
        }

        // 중복 검사를 통과하면 회원가입 진행
        Member member = new Member();
        member.setUsername(signupDTO.getUsername());
        member.setPassword(bCryptPasswordEncoder.encode(signupDTO.getPassword()));
        member.setPersonName(signupDTO.getPersonName());
        member.setNickName(signupDTO.getNickName());
        member.setEmail(signupDTO.getEmail());
        member.setPhone(signupDTO.getPhone());  // 전화번호 추가

        // 기본적으로 ROLE_GUEST 설정
        member.setRole(Member.Role.ROLE_GUEST);

        // 회원 정보 저장
        member = memberRepository.save(member);
        return member.getMemberId(); // 사용자 ID 반환
    }

}
