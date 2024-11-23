//package com.store.velvetbloom.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.store.velvetbloom.model.Product;
//import com.store.velvetbloom.service.ProductService;
//import com.store.velvetbloom.util.JwtUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ProductController.class)
//@Import(TestSecurityConfig.class)
//public class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductService productService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    private ObjectMapper objectMapper;
//    private Product sampleProduct;
//    private String adminToken;
//    private String userToken;
//
//    @BeforeEach
//    public void setup() {
//        objectMapper = new ObjectMapper();
//        adminToken = jwtUtil.generateToken("admin@test.com", "ROLE_ADMIN");
//        userToken = jwtUtil.generateToken("user@test.com", "ROLE_USER");
//
//        // Initialize sample product
//        sampleProduct = new Product();
//        sampleProduct.setId("673bca3d94cb650c9b688e13");
//        sampleProduct.setProductName("Running Shoes");
//        sampleProduct.setDescription("Lightweight and durable running shoes for all terrains.");
//        sampleProduct.setBrand("ActiveGear");
//        sampleProduct.setDiscount(15);
//        sampleProduct.setUnitPrice(50.0);
//        sampleProduct.setProductCount(200);
//        sampleProduct.setLowStockCount(20);
//        sampleProduct.setMainImgUrl("https://example.com/images/shoes-main.jpg");
//        sampleProduct.setImageGallery(List.of(
//                "https://example.com/images/shoes-side.jpg",
//                "https://example.com/images/shoes-top.jpg"
//        ));
//        sampleProduct.setCategories(List.of("Footwear", "Unisex", "Running"));
//
//        // Create varieties with proper color initialization
//        List<Product.Variety> varieties = new ArrayList<>();
//
//        Product.Variety variety1 = new Product.Variety();
//        variety1.setSize("8");
//        List<Product.Variety.Color> colors1 = new ArrayList<>();
//
//        Product.Variety.Color white = new Product.Variety.Color();
//        white.setColor("White");
//        white.setCount(30);
//
//        Product.Variety.Color black = new Product.Variety.Color();
//        black.setColor("Black");
//        black.setCount(40);
//
//        colors1.add(white);
//        colors1.add(black);
//        variety1.setColors(colors1);
//
//        Product.Variety variety2 = new Product.Variety();
//        variety2.setSize("9");
//        List<Product.Variety.Color> colors2 = new ArrayList<>();
//
//        Product.Variety.Color gray = new Product.Variety.Color();
//        gray.setColor("Gray");
//        gray.setCount(20);
//
//        colors2.add(gray);
//        variety2.setColors(colors2);
//
//        varieties.add(variety1);
//        varieties.add(variety2);
//        sampleProduct.setVariety(varieties);
//
//        Product.Review review = new Product.Review();
//        review.setCustomerID("652f7d5f3c1e7f2b98765432");
//        review.setFName("Alice");
//        review.setDescription("Best shoes for running, highly recommend!");
//        sampleProduct.setReviews(List.of(review));
//
//        sampleProduct.setCreatedAt("2024-11-18T10:30:00Z");
//        sampleProduct.setUpdatedAt("2024-11-18T10:30:00Z");
//    }
//
//    private MockMultipartHttpServletRequestBuilder addAuth(MockMultipartHttpServletRequestBuilder request, String token) {
//        return request.header("Authorization", "Bearer " + token);
//    }
//
//
//    @Test
//    public void testGetAllProducts() throws Exception {
//        Mockito.when(productService.getAllProducts()).thenReturn(Collections.singletonList(sampleProduct));
//
//        mockMvc.perform(get("/products"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].id").value(sampleProduct.getId()))
//                .andExpect(jsonPath("$[0].productName").value(sampleProduct.getProductName()));
//    }
//
//    @Test
//    public void testGetProductById() throws Exception {
//        Mockito.when(productService.getProductById(sampleProduct.getId())).thenReturn(sampleProduct);
//
//        mockMvc.perform(get("/products/{id}", sampleProduct.getId()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(sampleProduct.getId()))
//                .andExpect(jsonPath("$.productName").value(sampleProduct.getProductName()));
//    }
//
//    @Test
//    public void testCreateProduct_WithoutAuth_ShouldFail() throws Exception {
//        MockMultipartFile productJson = new MockMultipartFile(
//                "product", "", "application/json",
//                objectMapper.writeValueAsString(sampleProduct).getBytes()
//        );
//
//        MockMultipartFile mainImage = new MockMultipartFile(
//                "mainImage", "main.jpg", "image/jpeg", "fake-image-data".getBytes()
//        );
//
//        mockMvc.perform(multipart("/products")
//                        .file(productJson)
//                        .file(mainImage)
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void testCreateProduct_WithAdminAuth_ShouldSucceed() throws Exception {
//        MockMultipartFile productJson = new MockMultipartFile(
//                "product", "", "application/json",
//                objectMapper.writeValueAsString(sampleProduct).getBytes()
//        );
//
//        MockMultipartFile mainImage = new MockMultipartFile(
//                "mainImage", "main.jpg", "image/jpeg", "fake-image-data".getBytes()
//        );
//
//        Mockito.when(productService.createProduct(any(Product.class), any(), any()))
//                .thenReturn(sampleProduct);
//
//        mockMvc.perform(addAuth(
//                        multipart("/products")
//                                .file(productJson)
//                                .file(mainImage)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf()), adminToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(sampleProduct.getId()))
//                .andExpect(jsonPath("$.productName").value(sampleProduct.getProductName()));
//    }
//
//    @Test
//    public void testUpdateProduct_WithAdminAuth_ShouldSucceed() throws Exception {
//        MockMultipartFile productJson = new MockMultipartFile(
//                "product", "", "application/json",
//                objectMapper.writeValueAsString(sampleProduct).getBytes()
//        );
//
//        MockMultipartFile mainImage = new MockMultipartFile(
//                "mainImage", "main.jpg", "image/jpeg", "fake-image-data".getBytes()
//        );
//
//        Mockito.when(productService.updateProduct(eq(sampleProduct.getId()), any(), any(), any()))
//                .thenReturn(sampleProduct);
//
//        mockMvc.perform(addAuth(
//                        multipart("/products/{id}", sampleProduct.getId())
//                                .file(productJson)
//                                .file(mainImage)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf()), adminToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(sampleProduct.getId()))
//                .andExpect(jsonPath("$.productName").value(sampleProduct.getProductName()));
//    }
//
//    @Test
//    public void testDeleteProduct_WithAdminAuth_ShouldSucceed() throws Exception {
//        Mockito.doNothing().when(productService).deleteProduct(sampleProduct.getId());
//
//        mockMvc.perform(addAuth(
//                        delete("/products/{id}", sampleProduct.getId())
//                                .with(SecurityMockMvcRequestPostProcessors.csrf()), adminToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Product deleted successfully"))
//                .andExpect(jsonPath("$.productId").value(sampleProduct.getId()));
//    }
//
//    @Test
//    public void testDeleteProduct_WithUserAuth_ShouldFail() throws Exception {
//        mockMvc.perform(addAuth(
//                        delete("/products/{id}", sampleProduct.getId())
//                                .with(SecurityMockMvcRequestPostProcessors.csrf()), userToken))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testDeleteProduct_WithoutAuth_ShouldFail() throws Exception {
//        mockMvc.perform(delete("/products/{id}", sampleProduct.getId())
//                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void testUpdateProduct_WithUserAuth_ShouldFail() throws Exception {
//        MockMultipartFile productJson = new MockMultipartFile(
//                "product", "", "application/json",
//                objectMapper.writeValueAsString(sampleProduct).getBytes()
//        );
//
//        mockMvc.perform(addAuth(
//                        multipart("/products/{id}", sampleProduct.getId())
//                                .file(productJson)
//                                .with(SecurityMockMvcRequestPostProcessors.csrf()), userToken))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testGetProductByInvalidId_ShouldReturn404() throws Exception {
//        Mockito.when(productService.getProductById("invalid-id")).thenReturn(null);
//
//        mockMvc.perform(get("/products/{id}", "invalid-id"))
//                .andExpect(status().isNotFound());
//    }
//}
