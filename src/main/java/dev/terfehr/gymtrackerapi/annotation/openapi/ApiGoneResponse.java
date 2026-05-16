package dev.terfehr.gymtrackerapi.annotation.openapi;

import dev.terfehr.gymtrackerapi.dto.error.ApiProblemDetailDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(
                responseCode = "410",
                description = "A verification token has expired or is no longer valid.",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ApiProblemDetailDTO.class),
                        examples = @ExampleObject(
                                name = "Verification token expired",
                                summary = "The token is no longer valid",
                                value = """
                                        {
                                          "type": "https://api.gymtracker.dev/problems/verification-expired",
                                          "title": "Verification expired",
                                          "status": 410,
                                          "detail": "The given email change code has expired! Please apply for a renewal!",
                                          "instance": "/auth/email/verify/abc123"
                                        }
                                        """
                        )
                )
        )
})
public @interface ApiGoneResponse {
}

