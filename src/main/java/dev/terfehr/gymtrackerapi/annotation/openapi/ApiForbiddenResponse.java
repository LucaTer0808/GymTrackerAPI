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
                responseCode = "403",
                description = "The authenticated user is not allowed to perform this action.",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ApiProblemDetailDTO.class),
                        examples = @ExampleObject(
                                name = "Access denied",
                                summary = "User is verified too late or lacks privileges",
                                value = """
                                        {
                                          "type": "https://api.gymtracker.dev/problems/forbidden",
                                          "title": "Forbidden",
                                          "status": 403,
                                          "detail": "Access is denied for the current user.",
                                          "instance": "/users/me/name"
                                        }
                                        """
                        )
                )
        )
})
public @interface ApiForbiddenResponse {
}

