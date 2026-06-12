package com.michael.ecommerce.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * Represents a product category in the e-commerce system.
 * Extends PanacheEntity which provides the 'id' field and
 * common database operations (persist, delete, find, etc.)
 */
@Entity
@Table(name = "categories")
public class Category extends PanacheEntity {

  @Column(name = "name", nullable = false, unique = true, length = 100)
  public String name;

  @Column(name = "description", length = 255)
  public String description;

  @Column(name = "active", nullable = false)
  public boolean active = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  public LocalDateTime createdAt;

  @Column(name = "updated_at")
  public LocalDateTime updatedAt;

  // ─── Lifecycle hooks ─────────────────────────────────────

  /**
   * Automatically sets timestamps before first insert.
   */
  @jakarta.persistence.PrePersist
  public void prePersist() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  /**
   * Automatically updates timestamp before every update.
   */
  @jakarta.persistence.PreUpdate
  public void preUpdate() {
    updatedAt = LocalDateTime.now();
  }

  // ─── Custom queries ──────────────────────────────────────

  /**
   * Finds a category by its name (case-insensitive).
   */
  public static Category findByName(String name) {
    return find("LOWER(name)", name.toLowerCase()).firstResult();
  }
}
