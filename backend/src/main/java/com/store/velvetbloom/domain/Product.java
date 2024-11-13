package com.store.velvetbloom.domain;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;


@Document
public class Product {

    @Id
    private UUID productId;

    @NotNull
    @Size(max = 100)
    private String productName;

    @Size(max = 255)
    private String sizeGuidImg;

    private String description;

    @Digits(integer = 7, fraction = 2)
    @Field(
            targetType = FieldType.DECIMAL128
    )
    private BigDecimal discount;

    @Size(max = 50)
    private String brand;

    @Digits(integer = 12, fraction = 2)
    @Field(
            targetType = FieldType.DECIMAL128
    )
    private BigDecimal unitPrice;

    @Size(max = 10)
    private String gender;

    @DocumentReference(lazy = true)
    private Category category;

    @DocumentReference(lazy = true, lookup = "{ 'product' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Variety> productVarieties;

    @DocumentReference(lazy = true, lookup = "{ 'product' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<SelectedItem> productSelectedItems;

    @DocumentReference(lazy = true, lookup = "{ 'product' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<OrderItem> productOrderItems;

    @DocumentReference(lazy = true, lookup = "{ 'product' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Review> productReviews;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(final UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public String getSizeGuidImg() {
        return sizeGuidImg;
    }

    public void setSizeGuidImg(final String sizeGuidImg) {
        this.sizeGuidImg = sizeGuidImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(final BigDecimal discount) {
        this.discount = discount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(final String brand) {
        this.brand = brand;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public Set<Variety> getProductVarieties() {
        return productVarieties;
    }

    public void setProductVarieties(final Set<Variety> productVarieties) {
        this.productVarieties = productVarieties;
    }

    public Set<SelectedItem> getProductSelectedItems() {
        return productSelectedItems;
    }

    public void setProductSelectedItems(final Set<SelectedItem> productSelectedItems) {
        this.productSelectedItems = productSelectedItems;
    }

    public Set<OrderItem> getProductOrderItems() {
        return productOrderItems;
    }

    public void setProductOrderItems(final Set<OrderItem> productOrderItems) {
        this.productOrderItems = productOrderItems;
    }

    public Set<Review> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(final Set<Review> productReviews) {
        this.productReviews = productReviews;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

}
