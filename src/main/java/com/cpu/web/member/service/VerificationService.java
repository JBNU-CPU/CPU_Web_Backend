package com.cpu.web.member.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationService {

    private final Map<String, String> verificationStorage = new HashMap<>();
    private final Map<String, Boolean> verifiedEmails = new HashMap<>(); // 인증된 이메일 저장

    // 인증 코드 저장
    public void saveVerificationCode(String email, String code) {
        verificationStorage.put(email, code);
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        return code.equals(verificationStorage.get(email));
    }

    // 이메일 인증 상태 저장
    public void markAsVerified(String email) {
        verifiedEmails.put(email, true);
    }

    // 이메일이 인증되었는지 확인
    public boolean isVerified(String email) {
        return verifiedEmails.getOrDefault(email, false);
    }
}
