package com.store.velvetbloom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
public class Image {

    @Id
    private String id;
    private String url;

    public Image() {}

    public Image(String url) {
        this.url = url;
    }

    // Getters and setters
}
