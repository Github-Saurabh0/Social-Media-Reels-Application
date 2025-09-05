package com.saurabhh.tiktokreels.controller;

import com.saurabhh.tiktokreels.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AIController {

    private final AIService aiService;
    
    /**
     * Generate a caption for a video using AI
     * 
     * @param videoFile The video file to generate a caption for
     * @return The generated caption
     */
    @PostMapping("/generate-caption")
    public ResponseEntity<Map<String, String>> generateCaption(@RequestParam("video") MultipartFile videoFile) {
        if (videoFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Video file is required"));
        }
        
        try {
            String caption = aiService.generateCaption(videoFile);
            Map<String, String> response = new HashMap<>();
            response.put("caption", caption);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to generate caption: " + e.getMessage()));
        }
    }
    
    /**
     * Generate a thumbnail for a video using AI
     * 
     * @param videoFile The video file to generate a thumbnail for
     * @return The generated thumbnail image
     */
    @PostMapping("/generate-thumbnail")
    public ResponseEntity<?> generateThumbnail(@RequestParam("video") MultipartFile videoFile) {
        if (videoFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Video file is required"));
        }
        
        try {
            byte[] thumbnailBytes = aiService.generateThumbnail(videoFile);
            
            if (thumbnailBytes.length == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to generate thumbnail"));
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(thumbnailBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to generate thumbnail: " + e.getMessage()));
        }
    }
}