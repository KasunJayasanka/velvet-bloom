package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Payment;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class PaymentListener extends AbstractMongoEventListener<Payment> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Payment> event) {
        if (event.getSource().getPaymentId() == null) {
            event.getSource().setPaymentId(UUID.randomUUID());
        }
    }

}
