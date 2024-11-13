package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Variety;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class VarietyListener extends AbstractMongoEventListener<Variety> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Variety> event) {
        if (event.getSource().getVarietyId() == null) {
            event.getSource().setVarietyId(UUID.randomUUID());
        }
    }

}
