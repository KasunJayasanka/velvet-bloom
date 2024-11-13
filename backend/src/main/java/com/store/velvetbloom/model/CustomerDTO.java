package com.store.velvetbloom.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;


public class CustomerDTO {

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

}
