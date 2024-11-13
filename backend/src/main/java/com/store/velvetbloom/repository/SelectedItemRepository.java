package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Cart;
import com.store.velvetbloom.domain.Product;
import com.store.velvetbloom.domain.SelectedItem;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface SelectedItemRepository extends MongoRepository<SelectedItem, UUID> {

    SelectedItem findFirstByCart(Cart cart);

    SelectedItem findFirstByProduct(Product product);

}
