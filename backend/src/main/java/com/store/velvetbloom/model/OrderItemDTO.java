package com.store.velvetbloom.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.UUID;


public class OrderItemDTO {

    private UUID orderItemId;

    private Integer quantity;

    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "95.08")
    private BigDecimal unitPrice;

    private UUID order;

    private UUID product;

    public UUID getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(final UUID orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public UUID getOrder() {
        return order;
    }

    public void setOrder(final UUID order) {
        this.order = order;
    }

    public UUID getProduct() {
        return product;
    }

    public void setProduct(final UUID product) {
        this.product = product;
    }

}
