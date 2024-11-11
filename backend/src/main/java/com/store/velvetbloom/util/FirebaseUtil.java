//package com.store.velvetbloom.util;
//
//import com.google.cloud.storage.Blob;
//import com.google.cloud.storage.Bucket;
//import com.google.firebase.cloud.StorageClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.InputStream;
//import java.util.UUID;
//
//@Component
//public class FirebaseUtil {
//
//    @Value("${firebase.cdn-url}")
//    private String cdnUrl;  // Add this to application.properties
//
//    public String uploadFile(InputStream fileStream, String fileName) {
//        Bucket bucket = StorageClient.getInstance().bucket();
//        String uniqueFileName = "images/" + UUID.randomUUID().toString() + "_" + fileName; // Specify the folder path here
//        Blob blob = bucket.create(uniqueFileName, fileStream, "image/jpeg");
//
//        // Return the CDN URL for the uploaded file
//        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucket.getName(), uniqueFileName.replace("/", "%2F"));
//
//        //return String.format("%s/images/%s", cdnUrl, uniqueFileName);
//    }
//}

package com.store.velvetbloom.util;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
public class FirebaseUtil {

    @Value("${firebase.cdn-url}") // URL for Firebase Hosting domain
    private String cdnUrl;

    public String uploadFile(InputStream fileStream, String fileName) {
        Bucket bucket = StorageClient.getInstance().bucket();
        String uniqueFileName = "images/" + UUID.randomUUID().toString() + "_" + fileName; // Specify the folder path here
        Blob blob = bucket.create(uniqueFileName, fileStream, "image/jpeg");

        // Return the CDN URL for the uploaded file
        return String.format("%s/images/%s", cdnUrl, uniqueFileName);
    }
}

