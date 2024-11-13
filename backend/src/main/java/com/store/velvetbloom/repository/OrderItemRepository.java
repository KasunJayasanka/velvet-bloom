package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.OrderItem;
import com.store.velvetbloom.domain.Orders;
import com.store.velvetbloom.domain.Product;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface OrderItemRepository extends MongoRepository<OrderItem, UUID> {

    OrderItem findFirstByOrder(Orders orders);

    OrderItem findFirstByProduct(Product product);

}
