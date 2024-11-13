package com.store.velvetbloom.domain;

import java.time.OffsetDateTime;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
public class Cart {

    @Id
    private Integer cartId;

    @DocumentReference(lazy = true)
    private Customer customer;

    @DocumentReference(lazy = true, lookup = "{ 'cart' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<SelectedItem> cartSelectedItems;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(final Integer cartId) {
        this.cartId = cartId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    public Set<SelectedItem> getCartSelectedItems() {
        return cartSelectedItems;
    }

    public void setCartSelectedItems(final Set<SelectedItem> cartSelectedItems) {
        this.cartSelectedItems = cartSelectedItems;
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
