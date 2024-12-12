package com.store.velvetbloom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.velvetbloom.model.Product;
import com.store.velvetbloom.service.ProductService;
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
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId("1");
        product1.setProductName("T-Shirt");
        product1.setDescription("Cotton T-Shirt");

        Product product2 = new Product();
        product2.setId("2");
        product2.setProductName("Jeans");
        product2.setDescription("Denim Jeans");

        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        // Act
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() {
        // Arrange
        Product product = new Product();
        product.setId("1");
        product.setProductName("T-Shirt");
        product.setDescription("Cotton T-Shirt");

        when(productService.getProductById("1")).thenReturn(product);

        // Act
        ResponseEntity<Product> response = productController.getProductById("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).getProductById("1");
    }

    @Test
    void testCreateProduct() throws Exception {
        // Arrange
        Product product = new Product();
        product.setProductName("T-Shirt");
        product.setDescription("Cotton T-Shirt");
        product.setUnitPrice(29.99);

        MockMultipartFile mainImage = new MockMultipartFile(
                "mainImage",
                "test.jpg",
                "image/jpeg",
                "test-image".getBytes()
        );

        MockMultipartFile galleryImage = new MockMultipartFile(
                "galleryImages",
                "gallery.jpg",
                "image/jpeg",
                "gallery-image".getBytes()
        );

        List<MockMultipartFile> galleryImages = Arrays.asList(galleryImage);
        String productJson = objectMapper.writeValueAsString(product);

        Product createdProduct = new Product();
        createdProduct.setId("1");
        createdProduct.setProductName(product.getProductName());
        createdProduct.setDescription(product.getDescription());
        createdProduct.setUnitPrice(product.getUnitPrice());

        when(productService.createProduct(any(Product.class), any(MockMultipartFile.class), anyList()))
                .thenReturn(createdProduct);

        // Act
        ResponseEntity<Product> response = productController.createProduct(
                productJson,
                mainImage,
                Arrays.asList(galleryImage)
        );

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(createdProduct, response.getBody());
        verify(productService, times(1))
                .createProduct(any(Product.class), any(MockMultipartFile.class), anyList());
    }

    @Test
    void testUpdateProduct() throws Exception {
        // Arrange
        Product product = new Product();
        product.setProductName("Updated T-Shirt");
        product.setDescription("Updated Cotton T-Shirt");
        product.setUnitPrice(39.99);

        MockMultipartFile mainImage = new MockMultipartFile(
                "mainImage",
                "updated.jpg",
                "image/jpeg",
                "updated-image".getBytes()
        );

        MockMultipartFile galleryImage = new MockMultipartFile(
                "galleryImages",
                "updated-gallery.jpg",
                "image/jpeg",
                "updated-gallery-image".getBytes()
        );

        String productJson = objectMapper.writeValueAsString(product);

        Product updatedProduct = new Product();
        updatedProduct.setId("1");
        updatedProduct.setProductName(product.getProductName());
        updatedProduct.setDescription(product.getDescription());
        updatedProduct.setUnitPrice(product.getUnitPrice());

        when(productService.updateProduct(eq("1"), any(Product.class), any(MockMultipartFile.class), anyList()))
                .thenReturn(updatedProduct);

        // Act
        ResponseEntity<Product> response = productController.updateProduct(
                "1",
                productJson,
                mainImage,
                Arrays.asList(galleryImage)
        );

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedProduct, response.getBody());
        verify(productService, times(1))
                .updateProduct(eq("1"), any(Product.class), any(MockMultipartFile.class), anyList());
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        String productId = "1";
        doNothing().when(productService).deleteProduct(productId);

        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(
                Map.of("message", "Product deleted successfully", "productId", productId),
                response.getBody()
        );
        verify(productService, times(1)).deleteProduct(productId);
    }
}