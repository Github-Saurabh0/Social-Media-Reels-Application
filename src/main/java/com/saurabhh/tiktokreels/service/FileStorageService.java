package com.saurabhh.tiktokreels.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    // Mock implementation for local development
    public FileStorageService() {
        // No initialization needed for mock implementation
    }

    public String uploadVideo(MultipartFile file) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            // Mock implementation returns a fake URL
            return "/api/videos/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public String uploadThumbnail(MultipartFile file) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            // Mock implementation returns a fake URL
            return "/api/thumbnails/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload thumbnail", e);
        }
    }
    
    /**
     * Uploads a generated thumbnail image
     * 
     * @param thumbnailBytes The thumbnail image bytes to upload
     * @param filename The filename to use for the thumbnail
     * @return The URL of the uploaded thumbnail
     */
    public String uploadGeneratedThumbnail(byte[] thumbnailBytes, String filename) {
        try {
            String key = "thumbnails/" + UUID.randomUUID().toString() + "-" + filename.replaceAll("\\s+", "-");
            // Mock implementation returns a fake URL
            return "/api/thumbnails/" + key;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload generated thumbnail", e);
        }
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "-" + originalFileName.replaceAll("\\s+", "-");
    }
}