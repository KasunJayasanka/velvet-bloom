package com.store.velvetbloom.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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


@Document
public class Customer {

    @Id
    private UUID customerId;

    @Size(max = 50)
    private String firstname;

    @Size(max = 50)
    private String lastname;

    @NotNull
    @Size(max = 100)
    private String email;

    @Size(max = 15)
    private String mobileNo;

    @Size(max = 255)
    private String firstAddress;

    @Size(max = 255)
    private String secondAddress;

    @Size(max = 50)
    private String district;

    @NotNull
    @Size(max = 255)
    private String password;

    @DocumentReference(lazy = true, lookup = "{ 'customer' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Cart> customerCarts;

    @DocumentReference(lazy = true, lookup = "{ 'customer' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Orders> customerOrderses;

    @DocumentReference(lazy = true, lookup = "{ 'customer' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Review> customerReviews;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final UUID customerId) {
        this.customerId = customerId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(final String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFirstAddress() {
        return firstAddress;
    }

    public void setFirstAddress(final String firstAddress) {
        this.firstAddress = firstAddress;
    }

    public String getSecondAddress() {
        return secondAddress;
    }

    public void setSecondAddress(final String secondAddress) {
        this.secondAddress = secondAddress;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(final String district) {
        this.district = district;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Set<Cart> getCustomerCarts() {
        return customerCarts;
    }

    public void setCustomerCarts(final Set<Cart> customerCarts) {
        this.customerCarts = customerCarts;
    }

    public Set<Orders> getCustomerOrderses() {
        return customerOrderses;
    }

    public void setCustomerOrderses(final Set<Orders> customerOrderses) {
        this.customerOrderses = customerOrderses;
    }

    public Set<Review> getCustomerReviews() {
        return customerReviews;
    }

    public void setCustomerReviews(final Set<Review> customerReviews) {
        this.customerReviews = customerReviews;
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
