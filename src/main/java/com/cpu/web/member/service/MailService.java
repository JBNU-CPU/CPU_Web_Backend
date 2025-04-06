package com.cpu.web.member.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setFrom("jbnucpu@gmail.com"); //보내는 사람 메일 주소
            helper.setSubject("CPU 이메일 확인 코드");

            //메일 내용
            String emailContent = """
            <p><b>전북대학교 중앙동아리 CPU에 가입해주셔서 감사합니다.</b></p>
            <p>귀하의 확인 코드는: <strong>%s</strong> 입니다.</p>
            """.formatted(verificationCode);

            helper.setText(emailContent, true); //HTML 내용

            mailSender.send(message); //메일 전송
            return verificationCode;
        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 실패: " + e.getMessage(), e);
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
