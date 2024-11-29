package com.store.velvetbloom.dto;

public class RecentOrder {
    private String id;
    private String customer;
    private String date;
    private String status;
    private String total;

    public RecentOrder(String id, String customer, String date, String status, String total) {
        this.id = id;
        this.customer = customer;
        this.date = date;
        this.status = status;
        this.total = total;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
