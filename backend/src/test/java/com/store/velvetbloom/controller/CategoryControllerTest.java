package com.store.velvetbloom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.velvetbloom.model.Category;
import com.store.velvetbloom.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        Category category1 = new Category();
        category1.setId("1");
        category1.setName("Men's Wear");
        category1.setDescription("Clothing for men");

        Category category2 = new Category();
        category2.setId("2");
        category2.setName("Women's Wear");
        category2.setDescription("Clothing for women");

        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act
        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(categories, response.getBody());
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void testGetCategoryById() {
        // Arrange
        Category category = new Category();
        category.setId("1");
        category.setName("Men's Wear");

        when(categoryService.getCategoryById("1")).thenReturn(category);

        // Act
        ResponseEntity<Category> response = categoryController.getCategoryById("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).getCategoryById("1");
    }

    @Test
    void testCreateCategory() throws Exception {
        // Arrange
        Category category = new Category();
        category.setName("Men's Wear");
        category.setDescription("Clothing for men");

        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test-image".getBytes());
        String categoryJson = objectMapper.writeValueAsString(category);

        Category createdCategory = new Category();
        createdCategory.setId("1");
        createdCategory.setName(category.getName());
        createdCategory.setDescription(category.getDescription());

        when(categoryService.createCategory(any(Category.class), any(MockMultipartFile.class))).thenReturn(createdCategory);

        // Act
        ResponseEntity<Category> response = categoryController.createCategory(categoryJson, imageFile);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(createdCategory, response.getBody());
        verify(categoryService, times(1)).createCategory(any(Category.class), any(MockMultipartFile.class));
    }

    @Test
    void testUpdateCategory() throws Exception {
        // Arrange
        Category category = new Category();
        category.setName("Men's Wear Updated");
        category.setDescription("Updated description");

        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpeg", "test-image".getBytes());
        String categoryJson = objectMapper.writeValueAsString(category);

        Category updatedCategory = new Category();
        updatedCategory.setId("1");
        updatedCategory.setName(category.getName());
        updatedCategory.setDescription(category.getDescription());

        when(categoryService.updateCategory(eq("1"), any(Category.class), any(MockMultipartFile.class))).thenReturn(updatedCategory);

        // Act
        ResponseEntity<Category> response = categoryController.updateCategory("1", categoryJson, imageFile);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedCategory, response.getBody());
        verify(categoryService, times(1)).updateCategory(eq("1"), any(Category.class), any(MockMultipartFile.class));
    }

    @Test
    void testAddProductToCategory() {
        // Arrange
        String categoryId = "1";
        String productId = "101";

        // Act
        ResponseEntity<?> response = categoryController.addProductToCategory(categoryId, productId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Map.of("message", "Product added to category", "categoryId", categoryId, "productId", productId), response.getBody());
        verify(categoryService, times(1)).addProductToCategory(categoryId, productId);
    }

    @Test
    void testRemoveProductFromCategory() {
        // Arrange
        String categoryId = "1";
        String productId = "101";

        // Act
        ResponseEntity<?> response = categoryController.removeProductFromCategory(categoryId, productId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Map.of("message", "Product removed from category", "categoryId", categoryId, "productId", productId), response.getBody());
        verify(categoryService, times(1)).removeProductFromCategory(categoryId, productId);
    }
}
