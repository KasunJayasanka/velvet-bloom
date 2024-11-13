package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Customer;
import com.store.velvetbloom.domain.Product;
import com.store.velvetbloom.domain.Review;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ReviewRepository extends MongoRepository<Review, UUID> {

    Review findFirstByCustomer(Customer customer);

    Review findFirstByProduct(Product product);

}
