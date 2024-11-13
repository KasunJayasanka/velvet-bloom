package com.store.velvetbloom.domain;

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
public class Variety {

    @Id
    private UUID varietyId;

    @Size(max = 20)
    private String size;

    private Integer totalAvailableCount;

    private Boolean availability;

    private Integer reorderCount;

    @DocumentReference(lazy = true)
    private Product product;

    @DocumentReference(lazy = true, lookup = "{ 'variety' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Color> varietyColors;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public Set<Color> getVarietyColors() {
        return varietyColors;
    }

    public void setVarietyColors(final Set<Color> varietyColors) {
        this.varietyColors = varietyColors;
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
