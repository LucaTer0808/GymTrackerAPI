package dev.terfehr.gymtrackerapi.exception;

public class DatabaseConflictException extends RuntimeException {
    public DatabaseConflictException(String message) {
        super(message);
    }
}
