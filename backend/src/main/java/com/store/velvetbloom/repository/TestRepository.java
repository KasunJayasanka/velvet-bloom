// TestRepository.java
package com.store.velvetbloom;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository extends MongoRepository<TestCollection, String> {
}
