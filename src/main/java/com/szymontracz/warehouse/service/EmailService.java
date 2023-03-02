package com.szymontracz.warehouse.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void send(String userEmail, String templateName, String confirmationUrl) throws MessagingException {
        if (!StringUtils.hasLength(templateName)) {
            templateName = "confirm-email";
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", userEmail);
        properties.put("confirmationUrl", confirmationUrl);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("welcome@szymontracz.com");
        helper.setTo(userEmail);
        helper.setSubject("Welcome to Portfolio Warehouse");

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }
}
