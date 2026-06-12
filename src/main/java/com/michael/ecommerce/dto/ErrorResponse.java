package com.michael.ecommerce.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Standard error response returned by the API on failures.
 * All errors follow this structure for consistency.
 */
@Schema(description = "Standard error response")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "404")
    public int status;

    @Schema(description = "Short error type", example = "Not Found")
    public String error;

    @Schema(description = "Human-readable error message", example = "Category with id 999 not found")
    public String message;

    @Schema(description = "Timestamp of the error")
    public LocalDateTime timestamp;

    public static ErrorResponse of(int status, String error, String message) {
        ErrorResponse response = new ErrorResponse();
        response.status    = status;
        response.error     = error;
        response.message   = message;
        response.timestamp = LocalDateTime.now();
        return response;
    }
}
