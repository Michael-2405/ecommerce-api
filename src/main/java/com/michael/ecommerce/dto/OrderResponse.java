package com.michael.ecommerce.dto;

import com.michael.ecommerce.entity.Order;
import com.michael.ecommerce.entity.OrderItem;
import com.michael.ecommerce.entity.OrderStatus;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    @Schema(description = "Order id", required = true)
    public Long id;

    @Schema(description = "Customer to whom this order belongs to", required = true)
    public CustomerResponse customer;

    @Schema(description = "Status of the order", required = true)
    public OrderStatus status;

    @Schema(description = "Subtotal of the whole order", required = true)
    public BigDecimal total;

    @Schema(description = "List of items of the order", required = true)
    public List<OrderItemResponse> items;

    @Schema(description = "Creation timestamp")
    public LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    public LocalDateTime updatedAt;

    public static OrderResponse from (Order order) {
        OrderResponse response = new OrderResponse();
        response.id = order.id;
        response.customer = CustomerResponse.from(order.customer);
        response.status = order.status;
        response.total = order.total;
        response.items = order.items.stream()
                                    .map(item -> OrderItemResponse.from(item))
                                    .toList();
        response.createdAt = order.createdAt;
        response.updatedAt = order.updatedAt;
        return response;
    }
}
