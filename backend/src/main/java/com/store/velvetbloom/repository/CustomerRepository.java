package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Customer;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CustomerRepository extends MongoRepository<Customer, UUID> {
}
