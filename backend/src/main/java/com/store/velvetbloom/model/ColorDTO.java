package com.store.velvetbloom.model;

import jakarta.validation.constraints.Size;
import java.util.UUID;


public class ColorDTO {

    private UUID colorId;

    @Size(max = 20)
    private String colorName;

    private Integer count;

    @Size(max = 255)
    private String imgUrl;

    private UUID variety;

    public UUID getColorId() {
        return colorId;
    }

    public void setColorId(final UUID colorId) {
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(final String colorName) {
        this.colorName = colorName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(final String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public UUID getVariety() {
        return variety;
    }

    public void setVariety(final UUID variety) {
        this.variety = variety;
    }

}
