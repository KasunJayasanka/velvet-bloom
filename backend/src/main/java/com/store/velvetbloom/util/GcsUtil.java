package com.store.velvetbloom.util;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class GcsUtil {

    private static final Logger LOGGER = Logger.getLogger(GcsUtil.class.getName());

    private final Storage storage;

    @Value("${gcs.bucket-name}")
    private String bucketName;

    @Value("${gcs.cdn-url}")
    private String cdnUrl;

    public GcsUtil(Storage storage) {
        this.storage = storage;
    }

    
//    public String uploadFile(InputStream fileStream, String fileName) {
//        // Prefix the filename with "images/" to upload into the images folder within the bucket
//        String uniqueFileName = "images/" + UUID.randomUUID().toString() + "_" + fileName;
//        Bucket bucket = storage.get(bucketName);
//        Blob blob = bucket.create(uniqueFileName, fileStream, "image/jpeg");
//
//        // Construct the URL, ensuring no duplicate slashes
//        return String.format("%s/%s", cdnUrl, uniqueFileName);
//    }

    public String uploadFile(InputStream fileStream, String fileName) {
        // Add "images/" prefix to upload into the images folder within the bucket
        String uniqueFileName = "images/" + UUID.randomUUID().toString() + "_" + fileName;
        Bucket bucket = storage.get(bucketName);
        Blob blob = bucket.create(uniqueFileName, fileStream, "image/jpeg");

        // Construct the URL, ensuring no duplicate slashes
        return String.format("%s/%s", cdnUrl.replaceAll("/$", ""), uniqueFileName);
    }


}
