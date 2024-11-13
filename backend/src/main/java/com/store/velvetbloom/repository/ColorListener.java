package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Color;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class ColorListener extends AbstractMongoEventListener<Color> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Color> event) {
        if (event.getSource().getColorId() == null) {
            event.getSource().setColorId(UUID.randomUUID());
        }
    }

}
