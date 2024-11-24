package com.store.velvetbloom.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.store.velvetbloom.exception.ResourceNotFoundException;
import com.store.velvetbloom.model.Category;
import com.store.velvetbloom.util.MultipartInputStreamFileResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.store.velvetbloom.repository.ProductRepository;
import com.store.velvetbloom.repository.CategoryRepository;
import com.store.velvetbloom.service.CategoryService;

import com.store.velvetbloom.model.Product;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    

    public Product createProduct(Product product, MultipartFile mainImage, List<MultipartFile> galleryImages) {
        // Upload main image
        String mainImageUrl = uploadImage(mainImage);
        product.setMainImgUrl(mainImageUrl);

        // Upload gallery images
        if (galleryImages != null && !galleryImages.isEmpty()) {
            List<String> galleryUrls = new ArrayList<>();
            for (MultipartFile image : galleryImages) {
                String galleryUrl = uploadImage(image);
                galleryUrls.add(galleryUrl);
            }
            product.setImageGallery(galleryUrls);
        }

        // Set timestamps
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
        product.setCreatedAt(timestamp);
        product.setUpdatedAt(timestamp);

        // Save product to the database
        // Save the product
        Product createdProduct = productRepository.save(product);

        // Update categories
        updateCategoriesWithProduct(createdProduct);

        return createdProduct;
    }

    private String uploadImage(MultipartFile image) {
        String uploadUrl = "http://localhost:8080/images/upload";
        try {
            // Create a multipart request
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResourceUtil(image.getInputStream(), image.getOriginalFilename()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Send request to upload API
            ResponseEntity<Map> response = restTemplate.postForEntity(uploadUrl, requestEntity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().get("imgUrl").toString();
            } else {
                throw new RuntimeException("Failed to upload image.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading image file.", e);
        }
    }

    public Product updateProduct(String id, Product product, MultipartFile mainImage, List<MultipartFile> galleryImages) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        // Update fields
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setUnitPrice(product.getUnitPrice());
        existingProduct.setProductCount(product.getProductCount());
        existingProduct.setLowStockCount(product.getLowStockCount());
        existingProduct.setCategories(product.getCategories());
        existingProduct.setVariety(product.getVariety());

        // Upload new images if provided
        if (mainImage != null) {
            String mainImageUrl = uploadImage(mainImage);
            existingProduct.setMainImgUrl(mainImageUrl);
        }
        if (galleryImages != null && !galleryImages.isEmpty()) {
            List<String> galleryUrls = galleryImages.stream()
                    .map(this::uploadImage)
                    .collect(Collectors.toList());
            existingProduct.setImageGallery(galleryUrls);
        }

        existingProduct.setUpdatedAt(LocalDateTime.now().toString());

        // Save the product
        Product updatedProduct = productRepository.save(existingProduct);

        // Update categories
        updateCategoriesWithProduct(updatedProduct);

        return updatedProduct;
    }


    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }


    private void updateCategoriesWithProduct(Product product) {
        List<String> categoryNames = product.getCategories(); // Retrieve category names from the product
        if (categoryNames == null || categoryNames.isEmpty()) {
            return; // Exit if no categories are associated with the product
        }

        // Fetch all categories by their names
        List<Category> categories = categoryRepository.findAllByNameIn(categoryNames);

        for (Category category : categories) {
            if (category.getProductIds() == null) {
                category.setProductIds(new ArrayList<>()); // Initialize if null
            }

            // Add the productId to the category if it's not already present
            if (!category.getProductIds().contains(product.getId())) {
                category.getProductIds().add(product.getId());
                category.setUpdatedAt(LocalDateTime.now().toString()); // Update the timestamp
            }
        }

        // Save all updated categories back to the repository
        categoryRepository.saveAll(categories);
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

    public void updateProductStock(Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + product.getId()));

        // Update stock count for each variety and color
        if (product.getVariety() != null) {
            for (Product.Variety newVariety : product.getVariety()) {
                Product.Variety existingVariety = existingProduct.getVariety().stream()
                        .filter(v -> v.getSize().equals(newVariety.getSize()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Variety not found for size: " + newVariety.getSize()));

                for (Product.Variety.Color newColor : newVariety.getColors()) {
                    Product.Variety.Color existingColor = existingVariety.getColors().stream()
                            .filter(c -> c.getColor().equals(newColor.getColor()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Color not found: " + newColor.getColor()));

                    // Adjust stock count
                    int updatedCount = existingColor.getCount() - newColor.getCount();
                    if (updatedCount < 0) {
                        throw new IllegalArgumentException("Insufficient stock for color: " + newColor.getColor());
                    }
                    existingColor.setCount(updatedCount);
                }
            }
        }

        // Save the updated product back to the repository
        productRepository.save(existingProduct);
    }

}
