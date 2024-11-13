package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Customer;
import com.store.velvetbloom.domain.Orders;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface OrdersRepository extends MongoRepository<Orders, UUID> {

    Orders findFirstByCustomer(Customer customer);

}
