package com.michael.ecommerce.service;

import com.michael.ecommerce.dto.CategoryRequest;
import com.michael.ecommerce.dto.CategoryResponse;
import com.michael.ecommerce.entity.Category;
import com.michael.ecommerce.exception.BusinessException;
import com.michael.ecommerce.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Service layer for category business logic.
 * All database operations go through this class.
 * The resource layer never touches the entity directly.
 */
@ApplicationScoped
public class CategoryService {

  /**
   * Returns all categories ordered by name.
   */
  public List<CategoryResponse> findAll() {
    return Category.listAll()
            .stream()
            .map(entity -> CategoryResponse.from((Category) entity))
            .toList();
  }

  /**
   * Finds a single category by ID.
   * Throws 404 if not found.
   */
  public CategoryResponse findById(Long id) {
    Category category = (Category) Category.findByIdOptional(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    return CategoryResponse.from(category);
  }

  /**
   * Creates a new category.
   * Validates that the name is not already taken.
   */
  @Transactional
  public CategoryResponse create(CategoryRequest request) {
    if (Category.findByName(request.name) != null) {
      throw new BusinessException(
              "A category with this name already exists: " + request.name
      );
    }

    Category category = new Category();
    category.name        = request.name;
    category.description = request.description;
    category.persist();

    return CategoryResponse.from(category);
  }

  /**
   * Updates an existing category by ID.
   * Validates name uniqueness excluding the current record.
   */
  @Transactional
  public CategoryResponse update(Long id, CategoryRequest request) {
    Category category = (Category) Category.findByIdOptional(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));

    // Check name conflict only if the name is actually changing
    if (!category.name.equalsIgnoreCase(request.name)) {
      if (Category.findByName(request.name) != null) {
        throw new BusinessException(
                "A category with this name already exists: " + request.name
        );
      }
    }

    category.name        = request.name;
    category.description = request.description;

    return CategoryResponse.from(category);
  }

  /**
   * Soft deletes a category by setting active = false.
   * We never hard delete — data integrity matters.
   */
  @Transactional
  public void delete(Long id) {
    Category category = (Category) Category.findByIdOptional(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    category.active = false;
  }
}
