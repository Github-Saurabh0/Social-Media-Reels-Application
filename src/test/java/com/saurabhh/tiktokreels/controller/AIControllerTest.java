package com.saurabhh.tiktokreels.controller;

import com.saurabhh.tiktokreels.service.AIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AIControllerTest {

    @Mock
    private AIService aiService;

    @InjectMocks
    private AIController aiController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(aiController).build();
    }

    @Test
    void generateCaption_shouldReturnCaption_whenVideoProvided() throws Exception {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        when(aiService.generateCaption(any())).thenReturn("AI generated caption for test video");

        // Act & Assert
        mockMvc.perform(multipart("/api/ai/generate-caption")
                .file(videoFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.caption").value("AI generated caption for test video"));
    }

    @Test
    void generateCaption_shouldReturnBadRequest_whenVideoIsEmpty() throws Exception {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
                "video",
                "",
                "video/mp4",
                new byte[0]
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/ai/generate-caption")
                .file(emptyFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Video file is required"));
    }

    @Test
    void generateCaption_shouldReturnServerError_whenServiceThrowsException() throws Exception {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        when(aiService.generateCaption(any())).thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        mockMvc.perform(multipart("/api/ai/generate-caption")
                .file(videoFile))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Failed to generate caption: Service error"));
    }

    @Test
    void generateThumbnail_shouldReturnThumbnail_whenVideoProvided() throws Exception {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        byte[] thumbnailBytes = "thumbnail image data".getBytes();
        when(aiService.generateThumbnail(any())).thenReturn(thumbnailBytes);

        // Act & Assert
        mockMvc.perform(multipart("/api/ai/generate-thumbnail")
                .file(videoFile))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(thumbnailBytes));
    }

    @Test
    void generateThumbnail_shouldReturnBadRequest_whenVideoIsEmpty() throws Exception {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
                "video",
                "",
                "video/mp4",
                new byte[0]
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/ai/generate-thumbnail")
                .file(emptyFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Video file is required"));
    }

    @Test
    void generateThumbnail_shouldReturnServerError_whenThumbnailGenerationFails() throws Exception {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        when(aiService.generateThumbnail(any())).thenReturn(new byte[0]);

        // Act & Assert
        mockMvc.perform(multipart("/api/ai/generate-thumbnail")
                .file(videoFile))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Failed to generate thumbnail"));
    }

    @Test
    void generateThumbnail_shouldReturnServerError_whenServiceThrowsException() throws Exception {
        // Arrange
        MockMultipartFile videoFile = new MockMultipartFile(
                "video",
                "test-video.mp4",
                "video/mp4",
                "test video content".getBytes()
        );

        when(aiService.generateThumbnail(any())).thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        mockMvc.perform(multipart("/api/ai/generate-thumbnail")
                .file(videoFile))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Failed to generate thumbnail: Service error"));
    }
}