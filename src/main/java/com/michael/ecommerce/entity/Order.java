package com.michael.ecommerce.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public OrderStatus status;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    public BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    public List<OrderItem> items;
}
