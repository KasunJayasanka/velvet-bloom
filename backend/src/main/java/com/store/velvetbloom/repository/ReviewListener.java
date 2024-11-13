package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Review;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class ReviewListener extends AbstractMongoEventListener<Review> {

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Review> event) {
        if (event.getSource().getReviewId() == null) {
            event.getSource().setReviewId(UUID.randomUUID());
        }
    }

}
