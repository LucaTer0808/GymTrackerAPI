package dev.terfehr.gymtrackerapi.annotation.openapi;

import dev.terfehr.gymtrackerapi.dto.error.ApiProblemDetailDTO;
import io.swagger.v3.oas.annotations.media.Content;
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
        @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiProblemDetailDTO.class))),
        @ApiResponse(responseCode = "401", description = "Authentication failed", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiProblemDetailDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiProblemDetailDTO.class))),
        @ApiResponse(responseCode = "404", description = "Referenced resource was not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiProblemDetailDTO.class))),
        @ApiResponse(responseCode = "409", description = "Conflict with the current state", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiProblemDetailDTO.class))),
        @ApiResponse(responseCode = "410", description = "Verification token is no longer valid", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiProblemDetailDTO.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected server error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiProblemDetailDTO.class)))
})
public @interface ApiProblemResponses {
}
