package com.michael.ecommerce.exception;

/**
 * Thrown when a requested resource does not exist in the database.
 * Maps to HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException (String resource, Long id) {
    super(resource + " with id " + id + " not found");
  }
}
