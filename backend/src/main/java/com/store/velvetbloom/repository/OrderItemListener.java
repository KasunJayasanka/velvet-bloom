package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.OrderItem;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class OrderItemListener extends AbstractMongoEventListener<OrderItem> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<OrderItem> event) {
        if (event.getSource().getOrderItemId() == null) {
            event.getSource().setOrderItemId(UUID.randomUUID());
        }
    }

}
