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
                responseCode = "401",
                description = "The request cannot be authenticated.",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ApiProblemDetailDTO.class),
                        examples = @ExampleObject(
                                name = "Authentication error",
                                summary = "Wrong credentials or invalid token",
                                value = """
                                        {
                                          "type": "https://api.gymtracker.dev/problems/authentication-failed",
                                          "title": "Authentication failed",
                                          "status": 401,
                                          "detail": "The given password does not match the users password",
                                          "instance": "/auth/login"
                                        }
                                        """
                        )
                )
        )
})
public @interface ApiUnauthorizedResponse {
}

