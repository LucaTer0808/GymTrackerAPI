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
                responseCode = "404",
                description = "The requested resource does not exist.",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ApiProblemDetailDTO.class),
                        examples = @ExampleObject(
                                name = "Resource not found",
                                summary = "The entity referenced by the id does not exist",
                                value = """
                                        {
                                          "type": "https://api.gymtracker.dev/problems/resource-not-found",
                                          "title": "Resource not found",
                                          "status": 404,
                                          "detail": "The execution with id: 42 does not exist",
                                          "instance": "/executions/42"
                                        }
                                        """
                        )
                )
        )
})
public @interface ApiNotFoundResponse {
}

