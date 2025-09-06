package com.saurabhh.tiktokreels.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AIServiceTest {

    @InjectMocks
    private AIService aiService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(aiService, "captionApiUrl", "https://api.example.com/caption");
        ReflectionTestUtils.setField(aiService, "captionApiKey", "test-api-key");
    }

    @Test
    void generateCaption_shouldReturnDefaultCaption_whenApiUnavailable() {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        // Act
        String caption = aiService.generateCaption(videoFile);

        // Assert
        assertNotNull(caption);
        assertTrue(caption.contains("Test Video"));
        assertTrue(caption.contains("#trending"));
    }
    
    @Test
    void generateCaption_shouldReturnApiCaption_whenApiAvailable() {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("caption", "AI generated caption for test video");
        
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(responseEntity);

        // Act
        String caption = aiService.generateCaption(videoFile);

        // Assert
        assertNotNull(caption);
        assertEquals("AI generated caption for test video", caption);
    }
    
    @Test
    void generateCaption_shouldReturnDefaultCaption_whenApiReturnsEmptyCaption() {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("caption", ""); // Empty caption
        
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(responseEntity);

        // Act
        String caption = aiService.generateCaption(videoFile);

        // Assert
        assertNotNull(caption);
        assertTrue(caption.contains("Test Video"));
        assertTrue(caption.contains("#trending"));
    }
    
    @Test
    void generateCaption_shouldReturnDefaultCaption_whenApiKeyIsEmpty() {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );
        
        // Set API key to empty
        ReflectionTestUtils.setField(aiService, "captionApiKey", "");

        // Act
        String caption = aiService.generateCaption(videoFile);

        // Assert
        assertNotNull(caption);
        assertTrue(caption.contains("Test Video"));
        assertTrue(caption.contains("#trending"));
    }

    @Test
    void generateThumbnail_shouldReturnThumbnailBytes() throws IOException {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        // Act
        byte[] thumbnail = aiService.generateThumbnail(videoFile);

        // Assert
        assertNotNull(thumbnail);
        assertTrue(thumbnail.length > 0);
    }
    
    @Test
    void generateThumbnail_shouldHandleNullFilename() throws IOException {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                null, // Null filename
                "video/mp4",
                "test video content".getBytes()
        );

        // Act
        byte[] thumbnail = aiService.generateThumbnail(videoFile);

        // Assert
        assertNotNull(thumbnail);
        assertTrue(thumbnail.length > 0);
    }
    
    @Test
    void generateThumbnail_shouldHandleLongFilename() throws IOException {
        // Arrange
        String longFilename = "this_is_a_very_long_filename_that_exceeds_the_20_character_limit_for_display_in_the_thumbnail.mp4";
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                longFilename,
                "video/mp4",
                "test video content".getBytes()
        );

        // Act
        byte[] thumbnail = aiService.generateThumbnail(videoFile);

        // Assert
        assertNotNull(thumbnail);
        assertTrue(thumbnail.length > 0);
    }
}