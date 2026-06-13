package com.michael.ecommerce.dto;

import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Request payload for creating or updating a product")
public class ProductRequest {

    @NotBlank(message = "The name is required")
    @Size(min = 2, max = 150, message = "The name must be between 2 and 150 characters")
    @Schema(description = "Product name", example = "iPhone 15", required = true)
    public String name;

    @Size(max = 500, message = "The description cannot exceed 500 characters")
    @Schema(description = "Product description", example = "Apple smartphone 128GB")
    public String description;

    @NotNull(message = "The price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Schema(description = "Product price", example = "999.99", required = true)
    public BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    @Schema(description = "Available stock units", example = "50")
    public int stock;

    @NotNull(message = "The category is required")
    @Schema(description = "Category ID the product belongs to", example = "1", required = true)
    public Long categoryId;
}
