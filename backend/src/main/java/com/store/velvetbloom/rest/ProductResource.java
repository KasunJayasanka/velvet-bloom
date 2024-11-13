package com.store.velvetbloom.rest;

import com.store.velvetbloom.model.ProductDTO;
import com.store.velvetbloom.service.ProductService;
import com.store.velvetbloom.util.ReferencedException;
import com.store.velvetbloom.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductResource {

    private final ProductService productService;

    public ProductResource(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProduct(
            @PathVariable(name = "productId") final UUID productId) {
        return ResponseEntity.ok(productService.get(productId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createProduct(@RequestBody @Valid final ProductDTO productDTO) {
        final UUID createdProductId = productService.create(productDTO);
        return new ResponseEntity<>(createdProductId, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<UUID> updateProduct(
            @PathVariable(name = "productId") final UUID productId,
            @RequestBody @Valid final ProductDTO productDTO) {
        productService.update(productId, productDTO);
        return ResponseEntity.ok(productId);
    }

    @DeleteMapping("/{productId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable(name = "productId") final UUID productId) {
        final ReferencedWarning referencedWarning = productService.getReferencedWarning(productId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

}