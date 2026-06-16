package com.michael.ecommerce.dto;

import com.michael.ecommerce.entity.OrderItem;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

public class OrderItemResponse {

    @Schema(description = "Product included in this order item")
    public ProductResponse product;

    @Schema(description = "Quantity ordered", example = "2")
    public int quantity;

    @Schema(description = "Unit price at the time of the order", example = "999.99")
    public BigDecimal unitPrice;

    @Schema(description = "Subtotal for this item (quantity x unitPrice)", example = "1999.98")
    public BigDecimal subtotal;

    public static OrderItemResponse from(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.product   = ProductResponse.from(orderItem.product);
        response.quantity  = orderItem.quantity;
        response.subtotal  = orderItem.unitPrice.multiply(BigDecimal.valueOf(orderItem.quantity));
        response.unitPrice = orderItem.unitPrice;
        return response;
    }
}
