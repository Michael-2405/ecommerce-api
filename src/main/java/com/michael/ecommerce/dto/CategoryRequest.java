package com.michael.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO for creating or updating a category.
 * Contains validation constraints and OpenAPI documentation.
 */

@Schema(description = "Request payload for creating or updating a category")
public class CategoryRequest {
  @NotBlank(message = "The name is required")
  @Size(min = 2, max = 100, message = "The name must contain between 2 and 100 characters")
  @Schema(description = "Category name", example = "Electronics", required = true)
  public String name;

  @Size(max = 255, message = "The description cannot be greater that 255 characters")
  @Schema(description = "Category description", example = "Electronic devices and accessories")
  public String description;
}
