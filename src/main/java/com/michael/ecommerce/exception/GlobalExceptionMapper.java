package com.michael.ecommerce.exception;

import com.michael.ecommerce.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Global exception handler for the API.
 * Intercepts exceptions thrown anywhere in the application
 * and converts them into clean, consistent HTTP responses.
 *
 * Without this, Quarkus returns 500 for all unhandled exceptions.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<RuntimeException> {

  @Override
  public Response toResponse(RuntimeException exception) {

    // 404 - Resource not found
    if (exception instanceof ResourceNotFoundException) {
      return Response
              .status(Response.Status.NOT_FOUND)
              .entity(ErrorResponse.of(404, "Not Found", exception.getMessage()))
              .build();
    }

    // 409 - Business rule violation (duplicate name, invalid state, etc.)
    if (exception instanceof BusinessException) {
      return Response
              .status(Response.Status.CONFLICT)
              .entity(ErrorResponse.of(409, "Conflict", exception.getMessage()))
              .build();
    }

    // 500 - Unexpected error (always log these in production)
    return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(ErrorResponse.of(500, "Internal Server Error", exception.getMessage()))
            .build();
  }
}
