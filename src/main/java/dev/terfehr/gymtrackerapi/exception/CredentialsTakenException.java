package dev.terfehr.gymtrackerapi.exception;

public class CredentialsTakenException extends RuntimeException {
    public CredentialsTakenException(String message) {
        super(message);
    }
}
