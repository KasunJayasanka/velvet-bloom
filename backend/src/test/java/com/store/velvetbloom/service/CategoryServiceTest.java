package com.store.velvetbloom.service;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Category;
import com.store.velvetbloom.model.Product;
import com.store.velvetbloom.repository.CategoryRepository;
import com.store.velvetbloom.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_ShouldReturnCategories() {
        // Arrange
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<Category> result = categoryService.getAllCategories();

        // Assert
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_ShouldReturnCategory_WhenIdExists() {
        // Arrange
        String categoryId = "123";
        Category category = new Category();
        category.setId(categoryId);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        Category result = categoryService.getCategoryById(categoryId);

        // Assert
        assertEquals(categoryId, result.getId());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void getCategoryById_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        String categoryId = "123";
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> categoryService.getCategoryById(categoryId));
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void createCategory_ShouldSaveCategoryWithImage() throws Exception {
        // Arrange
        Category category = new Category();
        MultipartFile imageFile = mock(MultipartFile.class);
        InputStream inputStream = new ByteArrayInputStream("image content".getBytes());
        String imageUrl = "http://image.url";

        when(imageFile.getInputStream()).thenReturn(inputStream);
        when(imageFile.getOriginalFilename()).thenReturn("image.jpg");
        when(imageService.uploadImage(inputStream, "image.jpg")).thenReturn(imageUrl);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Category result = categoryService.createCategory(category, imageFile);

        // Assert
        assertNotNull(result.getImageUrl());
        assertEquals(imageUrl, result.getImageUrl());
        verify(imageService, times(1)).uploadImage(any(InputStream.class), eq("image.jpg"));
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void addProductToCategory_ShouldAddProduct() {
        // Arrange
        String categoryId = "123";
        String productId = "456";

        Category category = new Category();
        category.setId(categoryId);
        category.setProductIds(new ArrayList<>());

        Product product = new Product(); // Replace with your actual Product class
        product.setId(productId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product)); // Fix: Return a Product object wrapped in Optional

        // Act
        categoryService.addProductToCategory(categoryId, productId);

        // Assert
        assertTrue(category.getProductIds().contains(productId));
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void removeProductFromCategory_ShouldRemoveProduct() {
        // Arrange
        String categoryId = "123";
        String productId = "456";

        Category category = new Category();
        category.setId(categoryId);
        List<String> productIds = new ArrayList<>();
        productIds.add(productId);
        category.setProductIds(productIds);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        categoryService.removeProductFromCategory(categoryId, productId);

        // Assert
        assertFalse(category.getProductIds().contains(productId));
        verify(categoryRepository, times(1)).save(category);
    }
}
