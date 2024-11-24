package com.store.velvetbloom.service;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Category;
import com.store.velvetbloom.model.Product;
import com.store.velvetbloom.repository.ProductRepository;
import com.store.velvetbloom.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RestTemplate restTemplate;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId("123");
        product.setProductName("Product A");
        product.setDescription("Product A Description");
        product.setBrand("Brand A");
        product.setDiscount(10);
        product.setUnitPrice(100.0);
        product.setProductCount(50);
        product.setLowStockCount(5);
        product.setCategories(Collections.singletonList("Category A"));
        product.setMainImgUrl("http://example.com/main.jpg");
        product.setImageGallery(Collections.singletonList("http://example.com/gallery.jpg"));
    }

    @Test
    void createProduct_Success() throws Exception {
        MockMultipartFile mainImage = new MockMultipartFile("mainImage", "main.jpg", "image/jpeg", "dummy content".getBytes());
        MockMultipartFile galleryImage = new MockMultipartFile("galleryImage", "gallery.jpg", "image/jpeg", "dummy content".getBytes());

        // Mock repository and rest template behavior
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(new ResponseEntity<>(Map.of("imgUrl", "http://example.com/uploaded.jpg"), HttpStatus.OK));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct(product, mainImage, List.of(galleryImage));

        assertNotNull(createdProduct);
        assertEquals("Product A", createdProduct.getProductName());
        assertEquals("http://example.com/uploaded.jpg", createdProduct.getMainImgUrl());
        assertEquals(1, createdProduct.getImageGallery().size());
        assertEquals("http://example.com/uploaded.jpg", createdProduct.getImageGallery().get(0));

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_Success() throws Exception {
        String productId = "123";
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setProductName("Updated Product A");
        updatedProduct.setDescription("Updated Product A Description");
        updatedProduct.setBrand("Updated Brand A");
        updatedProduct.setDiscount(15);
        updatedProduct.setUnitPrice(120.0);
        updatedProduct.setProductCount(100);
        updatedProduct.setLowStockCount(10);
        updatedProduct.setCategories(Collections.singletonList("Updated Category A"));

        MockMultipartFile mainImage = new MockMultipartFile("mainImage", "main.jpg", "image/jpeg", "dummy content".getBytes());
        MockMultipartFile galleryImage = new MockMultipartFile("galleryImage", "gallery.jpg", "image/jpeg", "dummy content".getBytes());

        // Mock repository and rest template behavior
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(Map.of("imgUrl", "http://example.com/uploaded.jpg"), HttpStatus.OK));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setMainImgUrl("http://example.com/uploaded.jpg");
            savedProduct.setImageGallery(Collections.singletonList("http://example.com/uploaded.jpg"));
            return savedProduct;
        });

        Product result = productService.updateProduct(productId, updatedProduct, mainImage, List.of(galleryImage));

        assertNotNull(result);
        assertEquals("Updated Product A", result.getProductName());
        assertEquals("http://example.com/uploaded.jpg", result.getMainImgUrl());
        assertEquals(1, result.getImageGallery().size());
        assertEquals("http://example.com/uploaded.jpg", result.getImageGallery().get(0));

        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct_Success() {
        String productId = "123";

        // Mock repository behavior
        doNothing().when(productRepository).deleteById(productId);

        // Call delete product method
        productService.deleteProduct(productId);

        // Verify delete behavior
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void getProductById_ProductNotFound() {
        String productId = "123";

        // Mock repository behavior to throw RuntimeException instead of ResourceNotFoundException
        when(productRepository.findById(productId))
                .thenThrow(new RuntimeException("Product not found"));

        // Call method and verify exception
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productService.getProductById(productId));
        assertEquals("Product not found", thrown.getMessage());
    }

    @Test
    void getAllProducts_Success() {
        // Mock repository behavior
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Product A", products.get(0).getProductName());
    }

}
