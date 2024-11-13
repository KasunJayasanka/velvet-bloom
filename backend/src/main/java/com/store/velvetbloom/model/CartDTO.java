package com.store.velvetbloom.model;

import java.util.UUID;


public class CartDTO {

    private Integer cartId;
    private UUID customer;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(final Integer cartId) {
        this.cartId = cartId;
    }

    public UUID getCustomer() {
        return customer;
    }

    public void setCustomer(final UUID customer) {
        this.customer = customer;
    }

}
