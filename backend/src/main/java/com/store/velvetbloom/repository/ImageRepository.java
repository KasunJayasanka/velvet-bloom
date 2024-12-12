package com.store.velvetbloom.repository;

import com.store.velvetbloom.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<Image, String> {
}
