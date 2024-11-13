package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Cart;
import com.store.velvetbloom.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class CartListener extends AbstractMongoEventListener<Cart> {

    private final PrimarySequenceService primarySequenceService;

    public CartListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Cart> event) {
        if (event.getSource().getCartId() == null) {
            event.getSource().setCartId(((int)primarySequenceService.getNextValue()));
        }
    }

}
