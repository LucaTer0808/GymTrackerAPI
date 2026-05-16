package dev.terfehr.gymtrackerapi.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(name = "ApiProblemDetail", description = "RFC 9457 Problem Details response for HTTP APIs.")
public record ApiProblemDetailDTO(
        @Schema(description = "A URI reference that identifies the problem type.", example = "https://api.gymtracker.dev/problems/validation-error")
        String type,

        @Schema(description = "Short, human-readable summary of the problem.", example = "Validation failed")
        String title,

        @Schema(description = "HTTP status code for the problem.", example = "400")
        Integer status,

        @Schema(description = "Human-readable explanation of the problem.", example = "One or more request fields are invalid.")
        String detail,

        @Schema(description = "The request path that caused the problem.", example = "/auth/register")
        String instance,

        @Schema(description = "Additional problem-specific properties, such as validation errors.")
        Map<String, String> errors
) {
}

