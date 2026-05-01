package dev.terfehr.gymtrackerapi.infrastructure;

import dev.terfehr.gymtrackerapi.controller.AuthController;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.email-address}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String email, String verificationCode) {
        final String verificationUrl = this.baseUrl + AuthController.VERIFY_PATH + "/" + verificationCode;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            helper.setTo(email);
            helper.setSubject("GymTracker - Bestätige deine Registrierung");
            helper.setFrom(from);

            String htmlContent = """
            <h3>Willkommen beim GymTracker!</h3>
            <p>Schön, dass du dabei bist. Bitte klicke auf den untenstehenden Link, um dein Konto zu verifizieren:</p>
            <a href="%s" style="padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;">Konto verifizieren</a>
            <p>Oder kopiere diesen Link in deinen Browser:</p>
            <p>%s</p>
        """.formatted(verificationUrl, verificationUrl);

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }
}
