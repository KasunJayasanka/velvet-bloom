// TestRepository.java
package com.store.velvetbloom.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.store.velvetbloom.model.TestCollection;

public interface TestRepository extends MongoRepository<TestCollection, String> {
}
