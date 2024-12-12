package com.store.velvetbloom.repository;

import com.store.velvetbloom.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findAllByNameIn(List<String> names); 
}

