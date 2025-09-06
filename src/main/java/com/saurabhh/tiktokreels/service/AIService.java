package com.saurabhh.tiktokreels.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {

    /**
     * Generates a caption for a video using AI
     * 
     * @param videoFile The video file to generate a caption for
     * @return The generated caption
     */
    public String generateCaption(MultipartFile videoFile) {
        return generateDefaultCaption(videoFile.getOriginalFilename());
    }
    
    /**
     * Generates a default caption based on the filename
     * 
     * @param filename The filename to generate a caption for
     * @return The generated caption
     */
    private String generateDefaultCaption(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "Check out my new video! #trending #viral";
        }
        
        // Clean up filename
        String cleanName = filename.replaceAll("\\.[^.]+$", "") // Remove extension
                .replaceAll("[_-]", " ") // Replace underscores and hyphens with spaces
                .replaceAll("\\d+", "") // Remove numbers
                .trim();
        
        // Capitalize first letter of each word
        String[] words = cleanName.split("\\s+");
        StringBuilder captionBuilder = new StringBuilder();
        
        for (String word : words) {
            if (!word.isEmpty()) {
                captionBuilder.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    captionBuilder.append(word.substring(1).toLowerCase());
                }
                captionBuilder.append(" ");
            }
        }
        
        // Add hashtags
        captionBuilder.append("| #trending #viral #reels");
        
        return captionBuilder.toString().trim();
    }
    
    /**
     * Generates a thumbnail from a video file
     * 
     * @param videoFile The video file to generate a thumbnail from
     * @return The generated thumbnail as a byte array
     */
    public byte[] generateThumbnail(MultipartFile videoFile) {
        try {
            // Create a blank image
            BufferedImage thumbnail = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = thumbnail.createGraphics();
            
            // Set background gradient
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(25, 118, 210), 
                thumbnail.getWidth(), thumbnail.getHeight(), new Color(66, 165, 245)
            );
            g.setPaint(gradient);
            g.fillRect(0, 0, thumbnail.getWidth(), thumbnail.getHeight());
            
            // Add text
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            String filename = videoFile.getOriginalFilename();
            String displayText = filename != null ? filename : "New Video";
            
            // Truncate if too long
            if (displayText.length() > 20) {
                displayText = displayText.substring(0, 17) + "...";
            }
            
            // Center text
            FontMetrics metrics = g.getFontMetrics();
            int x = (thumbnail.getWidth() - metrics.stringWidth(displayText)) / 2;
            int y = ((thumbnail.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
            
            g.drawString(displayText, x, y);
            
            // Add play button icon
            int iconSize = 100;
            int iconX = (thumbnail.getWidth() - iconSize) / 2;
            int iconY = y + 100;
            
            g.setColor(new Color(255, 255, 255, 180));
            g.fillOval(iconX, iconY, iconSize, iconSize);
            
            g.setColor(new Color(25, 118, 210));
            int[] xPoints = {iconX + iconSize/4, iconX + iconSize/4, iconX + 3*iconSize/4};
            int[] yPoints = {iconY + iconSize/4, iconY + 3*iconSize/4, iconY + iconSize/2};
            g.fillPolygon(xPoints, yPoints, 3);
            
            g.dispose();
            
            // Convert to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpg", baos);
            return baos.toByteArray();
            
        } catch (IOException e) {
            log.error("Error generating thumbnail: {}", e.getMessage());
            return new byte[0];
        }
    }
}