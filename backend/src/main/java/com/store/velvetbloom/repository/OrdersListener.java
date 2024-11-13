package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Orders;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class OrdersListener extends AbstractMongoEventListener<Orders> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Orders> event) {
        if (event.getSource().getOrderId() == null) {
            event.getSource().setOrderId(UUID.randomUUID());
        }
    }

}
