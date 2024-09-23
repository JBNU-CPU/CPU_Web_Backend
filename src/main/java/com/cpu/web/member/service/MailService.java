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
            // SimpleMailMessage 객체를 사용하여 이메일 전송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setFrom("slyhyun@naver.com"); // 보내는 사람 이메일 (네이버 계정)
            message.setSubject("Your Email Verification Code");
            message.setText("Your verification code is: " + verificationCode);

            mailSender.send(message);  // 이메일 전송
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to send email.", e);
        }

        return verificationCode;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}

