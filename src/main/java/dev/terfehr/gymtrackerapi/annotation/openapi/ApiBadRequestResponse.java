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
                responseCode = "400",
                description = "The request body is invalid or cannot be processed.",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ApiProblemDetailDTO.class),
                        examples = @ExampleObject(
                                name = "Validation error",
                                summary = "Missing or invalid fields",
                                value = """
                                        {
                                          "type": "https://api.gymtracker.dev/problems/validation-error",
                                          "title": "Validation failed",
                                          "status": 400,
                                          "detail": "One or more request fields are invalid.",
                                          "instance": "/auth/register",
                                          "errors": {
                                            "email": "must be a well-formed email address",
                                            "password": "size must be between 8 and 128"
                                          }
                                        }
                                        """
                        )
                )
        )
})
public @interface ApiBadRequestResponse {
}

