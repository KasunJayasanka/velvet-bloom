package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Category;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CategoryRepository extends MongoRepository<Category, UUID> {
}
