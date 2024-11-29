package com.store.velvetbloom.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.store.velvetbloom.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategories(String category);// Find products by category ID
    long countByProductCountGreaterThan(int count);
    long countByProductCountBetween(int min, int max);
    long countByProductCount(int count);
    List<Product> findByProductCountLessThan(int count);
}


