package com.phenikaa.jobhuntly.event.listener;

import com.phenikaa.jobhuntly.entity.User;
import com.phenikaa.jobhuntly.enums.TokenType;
import com.phenikaa.jobhuntly.event.RegistrationCompleteEvent;
import com.phenikaa.jobhuntly.service.TokenService;
import com.phenikaa.jobhuntly.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final TokenService tokenService;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String USERNAME;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newLy registered user
        User user = (User) event.getSource();

        // 2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();

        // 3. Save the verification token for the user
        tokenService.saveToken(user, verificationToken, TokenType.VERIFICATION_TOKEN);
        // 4. Build the verification url to sent to the user
        String url = event.getAppUrl() + "/register/verifyEmail?token=" + verificationToken;

        // 5. Sent the email
        try {
            sendMail(user, url);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendMail(User toUser, String url) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        Context context = new Context();
        context.setVariable("username", toUser.getUsername());
        context.setVariable("url", url);
        String process = templateEngine.process("mails/email_verification", context);
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom(new InternetAddress(USERNAME));
        messageHelper.setSubject("Mail xac nhan dang ki");
        messageHelper.setTo(toUser.getEmail());
        messageHelper.setText(process, true);

        mailSender.send(message);
    }
}
