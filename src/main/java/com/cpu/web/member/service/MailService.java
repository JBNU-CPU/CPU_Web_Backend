package com.cpu.web.member.service;

import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Service
public class MailService {

    private final MailgunMessagesApi mailgunMessagesApi;
    private final SpringTemplateEngine templateEngine;

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.sender}")
    private String sender;

    public MailService(@Value("${mailgun.api-key}") String apiKey, SpringTemplateEngine templateEngine) {
        this.mailgunMessagesApi = MailgunClient.config(apiKey).createApi(MailgunMessagesApi.class);
        this.templateEngine = templateEngine;
    }

    public String sendVerificationCode(String toEmail) {
        String verificationCode = generateVerificationCode();

        try {
            Context context = new Context();
            context.setVariable("verificationCode", verificationCode);

            //템플릿 엔진 작동시켜 변수(인증코드) 넣기
            String htmlContent = templateEngine.process("email", context);

            Message message = Message.builder()
                    .from(sender)
                    .to(toEmail)
                    .subject("CPU 이메일 확인 코드")
                    .html(htmlContent)
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
