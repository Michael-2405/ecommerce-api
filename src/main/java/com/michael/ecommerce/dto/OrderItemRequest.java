package com.michael.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class OrderItemRequest {

    @NotNull(message = "Product id is required")
    @Schema(description = "Product id", example = "1", required = true)
    public Long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "Quantity to order", example = "2", required = true)
    public int quantity;
}
