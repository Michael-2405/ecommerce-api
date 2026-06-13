package com.michael.ecommerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    public String name;

    @Column(name = "description", length = 500)
    public String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    public BigDecimal price;

    @Column(name = "stock", nullable = false)
    public int stock = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    public Category category;

    public static List<Product> findActive(Long categoryId) {
        if (categoryId != null) {
            return list("active = true AND category.id = ?1", categoryId);
        }
        return list("active", true);
    }
}
