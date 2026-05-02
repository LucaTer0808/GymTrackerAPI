package dev.terfehr.gymtrackerapi.infrastructure;

public class DevEmailService implements EmailServiceI {
    public void sendRegistrationEmail(String email, String verificationCode) {
        System.out.println("Sending registration email to " + email + " with verification code " + verificationCode);
    }

    public void sendVerificationEmail(String email) {
        System.out.println("Sending verification email to " + email);
    }

    public void sendRequestPasswordChangeEmail(String email, String passwordChangeCode) {
        System.out.println("Sending password change request email to " + email + " with code " + passwordChangeCode);
    }
}
