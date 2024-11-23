//package com.store.velvetbloom.controller;
//
//import com.store.velvetbloom.service.ImageService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class ImageControllerTest {
//
//    @Mock
//    private ImageService imageService;
//
//    @InjectMocks
//    private ImageController imageController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testUploadImage() throws IOException {
//        // Prepare mock file
//        MockMultipartFile mockFile = new MockMultipartFile(
//                "file",
//                "test-image.jpg",
//                "image/jpeg",
//                "Test image content".getBytes()
//        );
//
//        // Mock service response
//        String mockImageUrl = "http://example.com/images/test-image.jpg";
//        when(imageService.uploadImage(mockFile.getInputStream(), mockFile.getOriginalFilename())).thenReturn(mockImageUrl);
//
//        // Call the controller method
//        ResponseEntity<Map<String, String>> response = imageController.uploadImage(mockFile);
//
//        // Assertions
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(mockImageUrl, response.getBody().get("imgUrl"));
//        verify(imageService, times(1)).uploadImage(mockFile.getInputStream(), mockFile.getOriginalFilename());
//    }
//}
