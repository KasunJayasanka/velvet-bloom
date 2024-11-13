package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Product;
import com.store.velvetbloom.domain.Variety;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface VarietyRepository extends MongoRepository<Variety, UUID> {

    Variety findFirstByProduct(Product product);

}
