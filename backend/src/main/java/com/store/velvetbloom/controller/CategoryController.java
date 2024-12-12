package com.store.velvetbloom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.velvetbloom.model.Category;
import com.store.velvetbloom.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/with-product-count")
    public List<Map<String, Object>> getCategoriesWithProductCount() {
        return categoryService.getCategoriesWithProductCount();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(
            @RequestPart("category") String categoryJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        ObjectMapper objectMapper = new ObjectMapper();
        Category category;
        try {
            category = objectMapper.readValue(categoryJson, Category.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format for category.", e);
        }

        Category createdCategory = categoryService.createCategory(category, imageFile);
        return ResponseEntity.ok(createdCategory);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(
            @PathVariable String id,
            @RequestPart("category") String categoryJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        ObjectMapper objectMapper = new ObjectMapper();
        Category category;
        try {
            category = objectMapper.readValue(categoryJson, Category.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format for category.", e);
        }

        Category updatedCategory = categoryService.updateCategory(id, category, imageFile);
        return ResponseEntity.ok(updatedCategory);
    }

    @PostMapping("/{categoryId}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addProductToCategory(@PathVariable String categoryId, @PathVariable String productId) {
        categoryService.addProductToCategory(categoryId, productId);
        return ResponseEntity.ok(Map.of("message", "Product added to category", "categoryId", categoryId, "productId", productId));
    }

    @DeleteMapping("/{categoryId}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeProductFromCategory(@PathVariable String categoryId, @PathVariable String productId) {
        categoryService.removeProductFromCategory(categoryId, productId);
        return ResponseEntity.ok(Map.of("message", "Product removed from category", "categoryId", categoryId, "productId", productId));
    }

    
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategoryById(categoryId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Category with ID " + categoryId + " has been deleted successfully.");
        return ResponseEntity.ok(response);
    }
}
