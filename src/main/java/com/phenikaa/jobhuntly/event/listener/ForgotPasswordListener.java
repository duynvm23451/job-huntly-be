package com.phenikaa.jobhuntly.event.listener;

import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.TokenType;
import com.phenikaa.jobhuntly.event.ForgotPasswordEvent;
import com.phenikaa.jobhuntly.service.TokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ForgotPasswordListener implements ApplicationListener<ForgotPasswordEvent> {

    private final TokenService tokenService;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String USERNAME;

    @Override
    public void onApplicationEvent(ForgotPasswordEvent event) {
        User user = (User) event.getSource();

        String token = UUID.randomUUID().toString();

        tokenService.saveToken(user, token, TokenType.RESET_PASSWORD_TOKEN);

        String url = event.getAppUrl() + "/forgot-password?token=" + token;

        try {
            sendMail(user, url);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendMail(User toUser, String url) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Context context = new Context();
        context.setVariable("username", toUser.getUsername());
        context.setVariable("url", url);
        String process = templateEngine.process("mails/email_forgot_password", context);
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(new InternetAddress(USERNAME));
        mimeMessageHelper.setTo(toUser.getEmail());
        mimeMessageHelper.setSubject("Quên mật khẩu");
        mimeMessageHelper.setText(process, true);

        mailSender.send(mimeMessage);
    }
}
