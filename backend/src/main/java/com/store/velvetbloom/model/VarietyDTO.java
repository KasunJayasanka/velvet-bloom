package com.store.velvetbloom.model;

import jakarta.validation.constraints.Size;
import java.util.UUID;


public class VarietyDTO {

    private UUID varietyId;

    @Size(max = 20)
    private String size;

    private Integer totalAvailableCount;

    private Boolean availability;

    private Integer reorderCount;

    private UUID product;

    public UUID getVarietyId() {
        return varietyId;
    }

    public void setVarietyId(final UUID varietyId) {
        this.varietyId = varietyId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(final String size) {
        this.size = size;
    }

    public Integer getTotalAvailableCount() {
        return totalAvailableCount;
    }

    public void setTotalAvailableCount(final Integer totalAvailableCount) {
        this.totalAvailableCount = totalAvailableCount;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(final Boolean availability) {
        this.availability = availability;
    }

    public Integer getReorderCount() {
        return reorderCount;
    }

    public void setReorderCount(final Integer reorderCount) {
        this.reorderCount = reorderCount;
    }

    public UUID getProduct() {
        return product;
    }

    public void setProduct(final UUID product) {
        this.product = product;
    }

}
