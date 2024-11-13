package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.SelectedItem;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class SelectedItemListener extends AbstractMongoEventListener<SelectedItem> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<SelectedItem> event) {
        if (event.getSource().getSelectedItemId() == null) {
            event.getSource().setSelectedItemId(UUID.randomUUID());
        }
    }

}
