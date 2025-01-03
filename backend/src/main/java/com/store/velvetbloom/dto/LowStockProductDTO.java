package com.store.velvetbloom.dto;

public class LowStockProductDTO {
    private String id;
    private String name;
    private int stock;

    public LowStockProductDTO(String id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
