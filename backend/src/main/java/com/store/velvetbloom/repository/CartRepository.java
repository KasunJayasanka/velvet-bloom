package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Cart;
import com.store.velvetbloom.domain.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CartRepository extends MongoRepository<Cart, Integer> {

    Cart findFirstByCustomer(Customer customer);

}
