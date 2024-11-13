package com.store.velvetbloom.model;

import java.util.UUID;


public class SelectedItemDTO {

    private UUID selectedItemId;
    private Integer quantity;
    private Integer cart;
    private UUID product;

    public UUID getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(final UUID selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCart() {
        return cart;
    }

    public void setCart(final Integer cart) {
        this.cart = cart;
    }

    public UUID getProduct() {
        return product;
    }

    public void setProduct(final UUID product) {
        this.product = product;
    }

}
