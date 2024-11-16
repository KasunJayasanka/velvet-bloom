package com.store.velvetbloom.dto;

public class UserResponseDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;
    private String role;
    private String country;
    private String city;
    private String address;
    private String secondaryAddress;

    // Constructor for setting fields
    public UserResponseDTO(String id, String firstName, String lastName, String email, String mobileNo,
                           String role, String country, String city, String address, String secondaryAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
        this.role = role;
        this.country = country;
        this.city = city;
        this.address = address;
        this.secondaryAddress = secondaryAddress;
    }

    // Getters and setters (optional if using only constructor)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSecondaryAddress() {
        return secondaryAddress;
    }

    public void setSecondaryAddress(String secondaryAddress) {
        this.secondaryAddress = secondaryAddress;
    }
}
