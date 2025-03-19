package com.cpu.web.member.service;

import com.cpu.web.member.dto.request.CheckDTO;
import com.cpu.web.member.dto.request.NewPasswordDTO;
import com.cpu.web.member.entity.Member;
import com.cpu.web.member.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<String> validateAndSendCode(@Valid CheckDTO checkDTO) {
        Optional<Member> member = memberRepository.findByUsername(checkDTO.getUsername());
        if (member.isPresent() && member.get().getEmail().equals(checkDTO.getEmail())) {
            // 이메일과 사용자 이름이 일치하는 경우, 인증 코드 전송 및 저장
            String response = verificationService.sendAndSaveVerificationCode(checkDTO.getEmail());
            return Optional.of(response);
        }
        return Optional.empty();
    }

    public Optional<String> resetPassword(@Valid NewPasswordDTO newPasswordDTO) {
        String email = newPasswordDTO.getEmail();
        String newPassword = newPasswordDTO.getPassword();

        // 이메일 인증 확인
        if (!verificationService.isVerified(email)) {
            return Optional.of("이메일이 인증되지 않았습니다.");
        }

        // 사용자를 이메일로 조회
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if (memberOptional.isPresent()) {
            Member updatedMember = memberOptional.get();

            // 비밀번호 암호화 후 업데이트
            updatedMember.setPassword(passwordEncoder.encode(newPassword));
            memberRepository.save(updatedMember);

            // 이메일 인증 상태 초기화
            verificationService.markAsNotVerified(email);

            return Optional.of("비밀번호가 성공적으로 재설정되었습니다.");
        }
        return Optional.of("사용자를 찾을 수 없습니다.");
    }
}
