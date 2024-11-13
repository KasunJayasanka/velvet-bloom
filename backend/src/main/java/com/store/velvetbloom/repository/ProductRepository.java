package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Category;
import com.store.velvetbloom.domain.Product;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProductRepository extends MongoRepository<Product, UUID> {

    Product findFirstByCategory(Category category);

}
