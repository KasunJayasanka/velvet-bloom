package com.store.velvetbloom.model;

import jakarta.validation.constraints.Size;
import java.util.UUID;


public class PaymentDTO {

    private UUID paymentId;

    @Size(max = 20)
    private String paymentMethod;

    private UUID order;

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(final UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(final String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public UUID getOrder() {
        return order;
    }

    public void setOrder(final UUID order) {
        this.order = order;
    }

}
