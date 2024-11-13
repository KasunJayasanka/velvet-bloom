package com.store.velvetbloom.model;

import java.util.UUID;


public class ReviewDTO {

    private UUID reviewId;
    private String description;
    private UUID customer;
    private UUID product;

    public UUID getReviewId() {
        return reviewId;
    }

    public void setReviewId(final UUID reviewId) {
        this.reviewId = reviewId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public UUID getCustomer() {
        return customer;
    }

    public void setCustomer(final UUID customer) {
        this.customer = customer;
    }

    public UUID getProduct() {
        return product;
    }

    public void setProduct(final UUID product) {
        this.product = product;
    }

}
