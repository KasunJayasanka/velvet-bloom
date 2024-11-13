package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Category;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class CategoryListener extends AbstractMongoEventListener<Category> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Category> event) {
        if (event.getSource().getCategoryId() == null) {
            event.getSource().setCategoryId(UUID.randomUUID());
        }
    }

}
