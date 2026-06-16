package com.michael.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

public class OrderRequest {

    @NotNull(message = "The customer id is required")
    @Schema(description = "Customer Id", example = "1", required = true)
    public Long customerId;

    @Valid
    @NotNull(message = "Items list is required")
    @Size(min = 1, message = "Order must contain at least one item")
    @Schema(description = "List of items in the order", required = true)
    public List<OrderItemRequest> items;
}
