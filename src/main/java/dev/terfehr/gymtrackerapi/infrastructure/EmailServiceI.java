package dev.terfehr.gymtrackerapi.infrastructure;

public interface EmailServiceI {
    void sendRegistrationEmail(String email, String verificationCode);
    void sendVerificationEmail(String email);
    void sendRequestPasswordChangeEmail(String email, String passwordChangeCode);
}
