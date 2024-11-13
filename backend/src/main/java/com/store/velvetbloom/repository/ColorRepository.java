package com.store.velvetbloom.repository;

import com.store.velvetbloom.domain.Color;
import com.store.velvetbloom.domain.Variety;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ColorRepository extends MongoRepository<Color, UUID> {

    Color findFirstByVariety(Variety variety);

}
