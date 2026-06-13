package com.michael.ecommerce.dto;

import com.michael.ecommerce.entity.Product;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Product data returned by the API")
public class ProductResponse {
    @Schema(description = "Product ID", example = "1")
    public Long id;

    @Schema(description = "Product name", example = "Iphone 15")
    public String name;

    @Schema(description = "Product description", example = "App smartphone 128GB")
    public String description;

    @Schema(description = "Product price", example = "999.99")
    public BigDecimal price;

    @Schema(description = "Available stock units", example = "50")
    public int stock;

    @Schema(description = "Whether the product is active ", example = "true")
    public boolean active;

    @Schema(description = "Category this product belongs to")
    public CategoryResponse category;

    @Schema(description = "Creation timestamp")
    public LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    public LocalDateTime updatedAt;

    public static ProductResponse from (Product product) {
        ProductResponse response = new ProductResponse();
        response.id          = product.id;
        response.name        = product.name;
        response.description = product.description;
        response.price       = product.price;
        response.stock       = product.stock;
        response.active      = product.active;
        response.category    = CategoryResponse.from(product.category);
        response.createdAt   = product.createdAt;
        response.updatedAt   = product.updatedAt;
        return response;
    }
}
