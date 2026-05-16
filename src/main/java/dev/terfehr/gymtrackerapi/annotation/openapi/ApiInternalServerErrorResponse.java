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
                responseCode = "500",
                description = "An unexpected error occurred and was handled globally.",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ApiProblemDetailDTO.class),
                        examples = @ExampleObject(
                                name = "Internal server error",
                                summary = "Fallback error from the global handler",
                                value = """
                                        {
                                          "type": "https://api.gymtracker.dev/problems/internal-server-error",
                                          "title": "Internal server error",
                                          "status": 500,
                                          "detail": "An unexpected error occurred!",
                                          "instance": "/exercises/99"
                                        }
                                        """
                        )
                )
        )
})
public @interface ApiInternalServerErrorResponse {
}

