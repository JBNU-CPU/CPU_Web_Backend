package com.cpu.web.service.member;

import com.cpu.web.dto.member.SignupDTO;
import com.cpu.web.entity.member.Member;
import com.cpu.web.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(SignupDTO signupDTO) {

        if (memberRepository.existsByUsername(signupDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Member member = new Member();
        
        member.setUsername(signupDTO.getUsername());
        member.setPassword(bCryptPasswordEncoder.encode(signupDTO.getPassword()));
        member.setPersonName(signupDTO.getPersonName());
        member.setRole("ROLE_USER");

        memberRepository.save(member);
    }

}
