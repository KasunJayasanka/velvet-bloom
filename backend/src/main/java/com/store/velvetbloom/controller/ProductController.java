package com.store.velvetbloom.controller;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.velvetbloom.dto.LowStockProductDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.store.velvetbloom.model.Product;
import com.store.velvetbloom.service.ProductService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Product> createProduct(
//            @RequestPart("product") @Valid Product product,
//            @RequestPart("mainImage") MultipartFile mainImage,
//            @RequestPart(value = "galleryImages", required = false) List<MultipartFile> galleryImages) {
//
//        Product createdProduct = productService.createProduct(product, mainImage, galleryImages);
//        return ResponseEntity.ok(createdProduct);
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart("mainImage") MultipartFile mainImage,
            @RequestPart(value = "galleryImages", required = false) List<MultipartFile> galleryImages) {

        // Convert productJson into Product object
        ObjectMapper objectMapper = new ObjectMapper();
        Product product;
        try {
            product = objectMapper.readValue(productJson, Product.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format for product.", e);
        }

        Product createdProduct = productService.createProduct(product, mainImage, galleryImages);
        return ResponseEntity.ok(createdProduct);
    }



    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(
            @PathVariable String id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "mainImage", required = false) MultipartFile mainImage,
            @RequestPart(value = "galleryImages", required = false) List<MultipartFile> galleryImages) {

        // Convert JSON to Product object
        ObjectMapper objectMapper = new ObjectMapper();
        Product product;
        try {
            product = objectMapper.readValue(productJson, Product.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format for product.", e);
        }

        // Update product using the service
        Product updatedProduct = productService.updateProduct(id, product, mainImage, galleryImages);
        return ResponseEntity.ok(updatedProduct);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully", "productId", id));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LowStockProductDTO> getLowStockProducts() {
        int lowStockThreshold = 4; // Define your threshold for low stock
        return productService.getLowStockProducts(lowStockThreshold);
    }

    
}

