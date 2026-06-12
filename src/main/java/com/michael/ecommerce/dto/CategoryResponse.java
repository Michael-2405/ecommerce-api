package com.michael.ecommerce.dto;

import com.michael.ecommerce.entity.Category;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO for returning category data to the client.
 * Never exposes the entity directly — this controls
 * exactly what fields the API returns.
 */
@Schema(description = "Category data returned by the API")
public class CategoryResponse {

  @Schema(description = "Category ID", example = "1")
  public Long id;

  @Schema(description = "Category name", example = "Electronics")
  public String name;

  @Schema(description = "Category description", example = "Electronic devices and accessories")
  public String description;

  @Schema(description = "Whether the category is active", example = "true")
  public boolean active;

  @Schema(description = "Creation timestamp")
  public LocalDateTime createdAt;

  @Schema(description = "Last update timestamp")
  public LocalDateTime updatedAt;

  // ─── Factory method ──────────────────────────────────────

  /**
   * Converts a Category entity into a CategoryResponse DTO.
   * This is the only place where the mapping happens.
   */
  public static CategoryResponse from(Category category) {
    CategoryResponse response = new CategoryResponse();
    response.id          = category.id;
    response.name        = category.name;
    response.description = category.description;
    response.active      = category.active;
    response.createdAt   = category.createdAt;
    response.updatedAt   = category.updatedAt;
    return response;
  }
}
