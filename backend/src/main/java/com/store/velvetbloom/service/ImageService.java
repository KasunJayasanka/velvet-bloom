//package com.store.velvetbloom.service;
//
//import com.store.velvetbloom.model.Image;
//import com.store.velvetbloom.repository.ImageRepository;
//import com.store.velvetbloom.util.FirebaseUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.InputStream;
//
//@Service
//public class ImageService {
//
//    @Autowired
//    private FirebaseUtil firebaseUtil;
//
//    @Autowired
//    private ImageRepository imageRepository;
//
//    public String uploadImage(InputStream fileStream, String fileName) {
//        // Upload file to Firebase
//        String imageUrl = firebaseUtil.uploadFile(fileStream, fileName);
//
//        // Save URL to MongoDB
//        Image image = new Image(imageUrl);
//        imageRepository.save(image);
//
//        return imageUrl;
//    }
//}

package com.store.velvetbloom.service;

import com.store.velvetbloom.model.Image;
import com.store.velvetbloom.repository.ImageRepository;
import com.store.velvetbloom.util.GcsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ImageService {

    private final GcsUtil gcsUtil;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(GcsUtil gcsUtil, ImageRepository imageRepository) {
        this.gcsUtil = gcsUtil;
        this.imageRepository = imageRepository;
    }

    public String uploadImage(InputStream fileStream, String fileName) {
        String imageUrl = gcsUtil.uploadFile(fileStream, fileName);

        Image image = new Image(imageUrl);
        imageRepository.save(image);

        return imageUrl;
    }
}

