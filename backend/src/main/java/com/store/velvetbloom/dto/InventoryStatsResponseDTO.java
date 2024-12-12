package com.store.velvetbloom.dto;

public class InventoryStatsResponseDTO {
    private int categories;
    private int totalProducts;
    private int inStock;
    private int lowStock;
    private int outOfStock;

    public InventoryStatsResponseDTO(int categories, int totalProducts, int inStock, int lowStock, int outOfStock) {
        this.categories = categories;
        this.totalProducts = totalProducts;
        this.inStock = inStock;
        this.lowStock = lowStock;
        this.outOfStock = outOfStock;
    }

    // Getters and Setters
    public int getCategories() {
        return categories;
    }

    public void setCategories(int categories) {
        this.categories = categories;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getLowStock() {
        return lowStock;
    }

    public void setLowStock(int lowStock) {
        this.lowStock = lowStock;
    }

    public int getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(int outOfStock) {
        this.outOfStock = outOfStock;
    }
}
