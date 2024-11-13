package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Product;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class ProductListener extends AbstractMongoEventListener<Product> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Product> event) {
        if (event.getSource().getProductId() == null) {
            event.getSource().setProductId(UUID.randomUUID());
        }
    }

}
