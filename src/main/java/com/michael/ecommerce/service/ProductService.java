package com.michael.ecommerce.service;

import com.michael.ecommerce.dto.ProductRequest;
import com.michael.ecommerce.dto.ProductResponse;
import com.michael.ecommerce.entity.Category;
import com.michael.ecommerce.entity.Product;
import com.michael.ecommerce.exception.ResourceNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ProductService {

    public List<ProductResponse> findAll(Long categoryId) {
        return Product.findActive(categoryId)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    public ProductResponse findById(Long id) {
        Product product = (Product) Product.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Category category = (Category) Category.findByIdOptional(request.categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId));

        Product product = new Product();
        product.name        = request.name;
        product.description = request.description;
        product.price       = request.price;
        product.stock       = request.stock;
        product.category    = category;
        product.persist();

        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request){
        Product product = (Product) Product.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        Category category = (Category) Category.findByIdOptional(request.categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId));

        product.name        = request.name;
        product.description = request.description;
        product.price       = request.price;
        product.stock       = request.stock;
        product.category    = category;

        return ProductResponse.from(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = (Product) Product.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        product.active = false;
    }
}
