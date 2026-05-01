package dev.terfehr.gymtrackerapi.infrastructure;

public class DevEmailService implements EmailServiceI {
    public void sendRegistrationEmail(String email, String verificationCode) {
        System.out.println("Sending registration email to " + email + " with verification code " + verificationCode);
    }

    public void sendVerificationEmail(String email) {
        System.out.println("Sending verification email to " + email);
    }
}
