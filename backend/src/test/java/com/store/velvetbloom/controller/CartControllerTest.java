package com.store.velvetbloom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.velvetbloom.model.Cart;
import com.store.velvetbloom.model.Order;
import com.store.velvetbloom.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private ObjectMapper objectMapper;
    private Cart testCart;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();

        // Initialize test cart
        testCart = new Cart();
        testCart.setId("cart123");
        testCart.setCustomerID("customer123");
        testCart.setProducts(new ArrayList<>());

        // Initialize test cart item
        Cart.CartItem cartItem = new Cart.CartItem();
        cartItem.setProductID("product123");
        cartItem.setProductName("Test Product");
        cartItem.setSize("M");
        cartItem.setPrice(29.99);
        cartItem.setMainImage("image-url");
        cartItem.setColors(Arrays.asList(new Cart.CartItem.Color("Red", 2)));

        testCart.getProducts().add(cartItem);

        // Initialize test order
        testOrder = new Order();
        testOrder.setContactName("John Doe");
        testOrder.setContactMail("john@example.com");
        testOrder.setContactNumber("1234567890");
        Order.ShippingAddress address = new Order.ShippingAddress();
        address.setAddressOne("123 Test St");
        address.setCity("Test City");
        address.setCountry("Test Country");
        testOrder.setShippingAddress(address);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createCart_Success() throws Exception {
        when(cartService.createCart(anyString())).thenReturn(testCart);

        mockMvc.perform(post("/carts/{customerID}", "customer123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart123"))
                .andExpect(jsonPath("$.customerID").value("customer123"));

        verify(cartService).createCart("customer123");
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void addProductToCart_Success() throws Exception {
        when(cartService.addProductToCart(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(testCart);

        mockMvc.perform(post("/carts/{customerID}/products/{productID}", "customer123", "product123")
                        .param("size", "M")
                        .param("color", "Red")
                        .param("count", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].productID").value("product123"))
                .andExpect(jsonPath("$.products[0].size").value("M"));

        verify(cartService).addProductToCart("customer123", "product123", "M", "Red", 2);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void removeProductFromCart_Success() throws Exception {
        when(cartService.removeProductFromCart(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(testCart);

        mockMvc.perform(delete("/carts/{customerID}/products/{productID}", "customer123", "product123")
                        .param("size", "M")
                        .param("color", "Red")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService).removeProductFromCart("customer123", "product123", "M", "Red");
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void viewCart_Success() throws Exception {
        when(cartService.viewCart(anyString())).thenReturn(testCart);

        mockMvc.perform(get("/carts/{customerID}", "customer123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerID").value("customer123"));

        verify(cartService).viewCart("customer123");
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void checkoutCart_Success() throws Exception {
        when(cartService.checkoutCart(anyString(), any(Order.class))).thenReturn(new Order());

        mockMvc.perform(post("/carts/{customerID}/checkout", "customer123")
                        .content(objectMapper.writeValueAsString(testOrder))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService).checkoutCart(eq("customer123"), any(Order.class));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void updateCartProduct_Success() throws Exception {
        Cart.CartItem updatedItem = new Cart.CartItem();
        updatedItem.setProductID("product123");
        updatedItem.setSize("L");
        updatedItem.setColors(Arrays.asList(new Cart.CartItem.Color("Blue", 3)));

        when(cartService.getCartById(anyString())).thenReturn(testCart);
        doNothing().when(cartService).updateCartProduct(any(Cart.class), anyString(), any(Cart.CartItem.class));

        mockMvc.perform(patch("/carts/{cartId}/products/{productId}", "cart123", "product123")
                        .content(objectMapper.writeValueAsString(updatedItem))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value("cart123"))
                .andExpect(jsonPath("$.productId").value("product123"));

        verify(cartService).getCartById("cart123");
        verify(cartService).updateCartProduct(any(Cart.class), eq("product123"), any(Cart.CartItem.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void accessAllowed_ForAdminUser() throws Exception {
        when(cartService.viewCart(anyString())).thenReturn(testCart);

        mockMvc.perform(get("/carts/{customerID}", "customer123"))
                .andExpect(status().isOk());
    }
}