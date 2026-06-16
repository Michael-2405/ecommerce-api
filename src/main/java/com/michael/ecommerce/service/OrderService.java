package com.michael.ecommerce.service;

import com.michael.ecommerce.dto.OrderItemRequest;
import com.michael.ecommerce.dto.OrderRequest;
import com.michael.ecommerce.dto.OrderResponse;
import com.michael.ecommerce.entity.*;
import com.michael.ecommerce.exception.BusinessException;
import com.michael.ecommerce.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class OrderService {

    public List<OrderResponse> findAll() {
        return Order.listAll()
                .stream()
                .map(entity -> OrderResponse.from((Order) entity))
                .toList();
    }

    public OrderResponse findById(Long id) {
        Order order = (Order) Order.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {

        Customer customer = (Customer) Customer.findByIdOptional(request.customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.customerId));

        Order order = new Order();
        order.status = OrderStatus.PENDING;
        order.customer = customer;

        BigDecimal total = BigDecimal.ZERO;

        order.items = new ArrayList<>();

        for (OrderItemRequest itemRequest: request.items) {

            Product product = (Product) Product.findByIdOptional(itemRequest.productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", itemRequest.productId));

            if (product.stock < itemRequest.quantity) {
                throw new BusinessException(
                        "Product '%s': requested %d units, only %d available."
                                .formatted(
                                        product.name,
                                        itemRequest.quantity,
                                        product.stock
                                )
                );
            }

            product.stock -= itemRequest.quantity;

            OrderItem item = new OrderItem();
            item.order = order;
            item.product = product;
            item.quantity = itemRequest.quantity;
            item.unitPrice = product.price;

            BigDecimal subtotal = item.unitPrice.multiply(BigDecimal.valueOf(item.quantity));
            total = total.add(subtotal);

            order.items.add(item);
        }

        order.total = total;
        order.persist();
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = (Order) Order.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        order.status = status;
        return OrderResponse.from(order);
    }

    @Transactional
    public void delete(Long id) {
        Order order = (Order) Order.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        order.active = false;
    }


}
