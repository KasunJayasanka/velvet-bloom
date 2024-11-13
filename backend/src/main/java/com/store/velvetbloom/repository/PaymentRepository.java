package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Orders;
import com.store.velvetbloom.domain.Payment;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PaymentRepository extends MongoRepository<Payment, UUID> {

    Payment findFirstByOrder(Orders orders);

}
