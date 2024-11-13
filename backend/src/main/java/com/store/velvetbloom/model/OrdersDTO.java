package com.store.velvetbloom.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


public class OrdersDTO {

    private UUID orderId;

    private LocalDate orderDate;

    @Size(max = 20)
    private String orderStatus;

    private LocalDate deliveredDate;

    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "52.08")
    private BigDecimal totalAmount;

    private UUID customer;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(final UUID orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDate getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(final LocalDate deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public UUID getCustomer() {
        return customer;
    }

    public void setCustomer(final UUID customer) {
        this.customer = customer;
    }

}
