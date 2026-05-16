package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@NullMarked
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final URI RESOURCE_NOT_FOUND_TYPE = URI.create("https://api.gymtracker.dev/problems/resource-not-found");
    private static final URI AUTHENTICATION_TYPE = URI.create("https://api.gymtracker.dev/problems/authentication-failed");
    private static final URI VERIFICATION_TYPE = URI.create("https://api.gymtracker.dev/problems/verification-expired");
    private static final URI VALIDATION_TYPE = URI.create("https://api.gymtracker.dev/problems/validation-error");
    private static final URI CONFLICT_TYPE = URI.create("https://api.gymtracker.dev/problems/conflict");
    private static final URI FORBIDDEN_TYPE = URI.create("https://api.gymtracker.dev/problems/forbidden");
    private static final URI INTERNAL_TYPE = URI.create("https://api.gymtracker.dev/problems/internal-server-error");

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        HttpServletRequest request) {
        return buildProblem(HttpStatus.NOT_FOUND, "Resource not found", exception.getMessage(), RESOURCE_NOT_FOUND_TYPE, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(AuthenticationException exception,
                                                                       HttpServletRequest request) {
        return buildProblem(HttpStatus.UNAUTHORIZED, "Authentication failed", exception.getMessage(), AUTHENTICATION_TYPE, request);
    }

    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<ProblemDetail> handleVerificationException(VerificationException exception,
                                                                     HttpServletRequest request) {
        return buildProblem(HttpStatus.GONE, "Verification expired", exception.getMessage(), VERIFICATION_TYPE, request);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ProblemDetail> handleExpiredJwtException(ExpiredJwtException exception,
                                                                   HttpServletRequest request) {
        ProblemDetail problem = createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Authentication token expired",
                "The provided JWT token is no longer valid.",
                AUTHENTICATION_TYPE,
                request
        );
        problem.setProperty("reason", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
        ProblemDetail problem = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more request fields are invalid.",
                VALIDATION_TYPE,
                request
        );

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String message = error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value";
            errors.put(error.getField(), message);
        });
        problem.setProperty("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException exception,
                                                                       HttpServletRequest request) {
        ProblemDetail problem = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                exception.getMessage(),
                VALIDATION_TYPE,
                request
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(DatabaseConflictException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DatabaseConflictException exception,
                                                                              HttpServletRequest request) {
        return buildProblem(HttpStatus.CONFLICT, "Conflict", exception.getMessage(), CONFLICT_TYPE, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException exception,
                                                                     HttpServletRequest request) {
        return buildProblem(HttpStatus.FORBIDDEN, "Forbidden", exception.getMessage(), FORBIDDEN_TYPE, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception exception,
                                                         HttpServletRequest request) {
        ProblemDetail problem = createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "An unexpected error occurred!",
                INTERNAL_TYPE,
                request
        );
        problem.setProperty("reason", exception.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    private ResponseEntity<ProblemDetail> buildProblem(HttpStatus status,
                                                       String title,
                                                       String detail,
                                                       URI type,
                                                       HttpServletRequest request) {
        return ResponseEntity.status(status).body(createProblemDetail(status, title, detail, type, request));
    }

    private ProblemDetail createProblemDetail(HttpStatus status,
                                              String title,
                                              String detail,
                                              URI type,
                                              HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail == null || detail.isBlank() ? status.getReasonPhrase() : detail);
        problem.setTitle(title);
        problem.setType(type);
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }
}
