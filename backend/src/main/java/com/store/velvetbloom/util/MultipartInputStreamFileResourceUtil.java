
package com.store.velvetbloom.util;

import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

public class MultipartInputStreamFileResourceUtil extends InputStreamResource {
    private final String filename;

    public MultipartInputStreamFileResourceUtil(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() {
        return -1; // Indicate unknown length
    }
}

