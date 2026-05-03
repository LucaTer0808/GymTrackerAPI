package dev.terfehr.gymtrackerapi.infrastructure;

import dev.terfehr.gymtrackerapi.controller.AuthController;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile("prod")
public class ProdEmailService implements  EmailServiceI {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.email-address}")
    private String from;

    public ProdEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String email, String verificationCode) {
        final String verificationUrl = this.baseUrl + AuthController.VERIFY_PATH + "/" + verificationCode;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            String subject = "GymTracker - Bestätige deine Registrierung";
            String htmlContent = """
                <h3>Willkommen beim GymTracker!</h3>
                <p>Schön, dass du dabei bist. Bitte klicke auf den untenstehenden Link, um dein Konto zu verifizieren:</p>
                <a href="%s" style="padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;">Konto verifizieren</a>
                <p>Oder kopiere diesen Link in deinen Browser:</p>
                <p>%s</p>
                """.formatted(verificationUrl, verificationUrl);

            configureMimeMessageHelper(helper, email, subject, htmlContent);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendVerificationEmail(String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            String subject = "GymTracker - Sie haben sich erfolgreich verifiziert";
            String htmlContent = """
                    <h3>Ihre Verifizierung war erfolgreich</h3>
                    <p>Sie können den GymTracker jetzt ohne Einschränkungen nutzen. Viel Spaß!</p>
                    """;

            configureMimeMessageHelper(helper, email, subject, htmlContent);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendRequestPasswordChangeEmail(String email, String changePasswordCode) {
        final String passwordChangeUrl = this.baseUrl + AuthController.CONFIRM_PASSWORD_RESET_PATH + "/" +  changePasswordCode;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            String subject = "GymTracker - Setze dein Password zurück";
            String htmlContent = """
                    <h3>Eine Anfrage zum Zurücksetzen deines Passworts ist bei uns eingegangen!</h3>
                    <p>Kommt diese Anfrage von dir? Falls ja, clicke auf den untenstehenden Link, um dein Passwort zurückzusetzen</p>
                    <a href="%s" style="padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;">Passwort zurücksetzen</a>
                    <p>Oder kopiere diesen Link in deinen Browser:</p>
                    <p>%s</p>
                    """.formatted(passwordChangeUrl, passwordChangeUrl);

            configureMimeMessageHelper(helper, email, subject, htmlContent);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendRequestEmailChangeEmail(String email, String emailChangeCode) {
        final String emailChangeUrl = this.baseUrl + AuthController.CONFIRM_EMAIL_CHANGE_PATH + "/" + emailChangeCode;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            String subject = "GymTracker - Verify your email change";
            String htmlContent = """
                    <h3>We received a request to change your email address</h3>
                    <p>Did you send this request? If so, please click the following link to verify your new email address</p>
                    <a href="%s" style="padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;">Passwort zurücksetzen</a>
                    <p>Oder kopiere diesen Link in deinen Browser:</p>
                    <p>%s</p>
                    """.formatted(emailChangeUrl, emailChangeUrl);

            configureMimeMessageHelper(helper, email, subject, htmlContent);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendAccountDeletionEmail(String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            String subject = "GymTracker - Your account has been deleted!";
            String htmlContent = """
                    <h3>We are sorry to see you go</h3>
                    <p>We hope you enjoyed GymTracker and would love to see you again!</p>
                    """;

            configureMimeMessageHelper(helper, email, subject, htmlContent);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void configureMimeMessageHelper(MimeMessageHelper helper, String email, String subject, String htmlContent) throws MessagingException {
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setFrom(from);
        helper.setText(htmlContent);
;    }
}
