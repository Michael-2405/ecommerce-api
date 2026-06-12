package com.michael.ecommerce.exception;

/**
 * Base exception for business rule violations.
 * Use this when the request is valid but breaks a business rule.
 * (e.g. trying to create a duplicate category name).
 */
public class BusinessException extends RuntimeException{
  public BusinessException(String message) {
    super(message);
  }
}
