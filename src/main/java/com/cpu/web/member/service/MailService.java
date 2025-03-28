package com.cpu.web.member.service;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailService {

    private final MailgunMessagesApi mailgunMessagesApi;

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.sender}")
    private String sender;

    public MailService(@Value("${mailgun.api-key}") String apiKey) {
        this.mailgunMessagesApi = MailgunClient.config(apiKey).createApi(MailgunMessagesApi.class);
    }

    public String sendVerificationCode(String toEmail) {
        String verificationCode = generateVerificationCode();

        try {
            Message message = Message.builder()
                    .from(sender)
                    .to(toEmail)
                    .subject("CPU 이메일 확인 코드")
                    .html(String.format("""
                        <p><b>전북대학교 중앙동아리 CPU에 가입해주셔서 감사합니다.</b></p>
                        <p>귀하의 확인 코드는: <strong>%s</strong> 입니다.</p>
                    """, verificationCode))
                    .build();

            mailgunMessagesApi.sendMessage(domain, message);
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
