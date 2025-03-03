package com.cpu.web.member.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendVerificationCode(String toEmail) {
        String verificationCode = generateVerificationCode();

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setFrom("slyhyun@naver.com"); // 보내는 사람 이메일 주소
            message.setSubject("이메일 인증 코드");
            message.setText("귀하의 인증 코드는: " + verificationCode);

            mailSender.send(message);  // 이메일 전송
            return verificationCode;
        } catch (Exception e) {
            throw new IllegalArgumentException("이메일 전송 실패: " + e.getMessage(), e);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
