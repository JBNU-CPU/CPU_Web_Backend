package com.cpu.web.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationService {

    private final Map<String, String> verificationStorage = new HashMap<>();
    private final Map<String, Boolean> verifiedEmails = new HashMap<>();

    @Autowired
    private MailService mailService;

    public String sendAndSaveVerificationCode(String email) {
        String code = mailService.sendVerificationCode(email);
        verificationStorage.put(email, code);
        return "인증 코드가 " + email + "로 전송되었습니다.";
    }

    public boolean verifyCode(String email, String code) {
        boolean isValid = code.equals(verificationStorage.get(email));
        if (isValid) {
            markAsVerified(email);
        } else {
            markAsNotVerified(email);
        }
        return isValid;
    }

    public void markAsVerified(String email) {
        verifiedEmails.put(email, true);
    }

    public void markAsNotVerified(String email) {
        verifiedEmails.remove(email);
    }

    public boolean isVerified(String email) {
        return verifiedEmails.getOrDefault(email, false);
    }
}
