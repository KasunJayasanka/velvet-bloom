package com.store.velvetbloom;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "testCollection")
public class TestCollection {
    @Id
    private String id;
    private String message;

    // getters and setters
}