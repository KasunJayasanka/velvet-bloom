package com.store.velvetbloom.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.store.velvetbloom.model.Category;
import com.store.velvetbloom.repository.CategoryRepository;
import com.store.velvetbloom.repository.ProductRepository;
import com.store.velvetbloom.service.ImageService;

import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, ImageService imageService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.imageService = imageService;
    }
    
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category createCategory(Category category, MultipartFile imageFile) {
        // Set createdAt and updatedAt timestamps
        category.setCreatedAt(LocalDateTime.now().toString());
        category.setUpdatedAt(LocalDateTime.now().toString());

        // If an image file is provided, upload it and set the image URL
        if (imageFile != null) {
            try (InputStream inputStream = imageFile.getInputStream()) { // Extract InputStream
                String imageUrl = imageService.uploadImage(inputStream, imageFile.getOriginalFilename());
                category.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image for category", e);
            }
        }

        // Save the category to the repository
        return categoryRepository.save(category);
    }


    public Category updateCategory(String id, Category category, MultipartFile imageFile) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        if (imageFile != null) {
            try (InputStream inputStream = imageFile.getInputStream()) { // Extract InputStream
                String imageUrl = imageService.uploadImage(inputStream, imageFile.getOriginalFilename());
                existingCategory.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image for category", e);
            }
        }

        existingCategory.setUpdatedAt(LocalDateTime.now().toString());
        return categoryRepository.save(existingCategory);
    }

    public void addProductToCategory(String categoryId, String productId) {
        // Fetch the category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        // Validate that the product exists
        productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        // Initialize the productIds list if null
        if (category.getProductIds() == null) {
            category.setProductIds(new ArrayList<>());
        }

        // Add the productId to the list if it's not already there
        if (!category.getProductIds().contains(productId)) {
            category.getProductIds().add(productId);
            category.setUpdatedAt(LocalDateTime.now().toString());
            categoryRepository.save(category);
        }
    }

    public void removeProductFromCategory(String categoryId, String productId) {
        // Fetch the category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        // Remove the productId from the list if it exists
        if (category.getProductIds() != null) {
            category.getProductIds().remove(productId);
            category.setUpdatedAt(LocalDateTime.now().toString());
            categoryRepository.save(category);
        }
    }

}