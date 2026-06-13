package com.michael.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

  @Column(name = "name", nullable = false, unique = true, length = 100)
  public String name;

  @Column(name = "description", length = 255)
  public String description;

  public static Category findByName(String name) {
    return find("LOWER(name)", name.toLowerCase()).firstResult();
  }
}
