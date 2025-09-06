package com.saurabhh.tiktokreels.service;

import com.saurabhh.tiktokreels.dto.ReelDTO;
import com.saurabhh.tiktokreels.model.Reel;
import com.saurabhh.tiktokreels.model.User;
import com.saurabhh.tiktokreels.repository.ReelRepository;
import com.saurabhh.tiktokreels.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReelService {
    private final ReelRepository reelRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final AIService aiService;
    
    public ReelDTO createReel(ReelDTO reelDTO) {
        User user = userRepository.findById(reelDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + reelDTO.getUserId()));
        
        // Create and save the reel
        Reel reel = Reel.builder()
                .userId(reelDTO.getUserId())
                .title(reelDTO.getTitle())
                .description(reelDTO.getDescription())
                .videoUrl(reelDTO.getVideoUrl())
                .thumbnailUrl(reelDTO.getThumbnailUrl())
                .durationMs(reelDTO.getDurationMs())
                .likesCount(0)
                .viewsCount(0)
                .isPrivate(reelDTO.isPrivate())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        
        Reel savedReel = reelRepository.save(reel);
        return convertToDTO(savedReel, user.getUsername());
    }
    
    public ReelDTO createReel(Long userId, String title, String description, String privacyStatus, 
                             MultipartFile videoFile, MultipartFile thumbnailFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        // Upload video to storage
        String videoUrl = fileStorageService.uploadVideo(videoFile);
        String thumbnailUrl = null;
        
        // If no thumbnail provided, generate one using AI
        if (thumbnailFile == null || thumbnailFile.isEmpty()) {
            try {
                byte[] generatedThumbnail = aiService.generateThumbnail(videoFile);
                if (generatedThumbnail.length > 0) {
                    thumbnailUrl = fileStorageService.uploadGeneratedThumbnail(generatedThumbnail, userId + "_" + System.currentTimeMillis() + ".jpg");
                }
            } catch (Exception e) {
                // Log error but continue without thumbnail
                System.err.println("Failed to generate thumbnail: " + e.getMessage());
            }
        } else {
            thumbnailUrl = fileStorageService.uploadThumbnail(thumbnailFile);
        }
        
        // If no description provided, generate one using AI
        if (description == null || description.isEmpty()) {
            try {
                String aiCaption = aiService.generateCaption(videoFile);
                // Mark AI-generated captions
                description = aiCaption + " [AI Generated]";
            } catch (Exception e) {
                // Log error but continue with empty description
                System.err.println("Failed to generate caption: " + e.getMessage());
                description = "";
            }
        }
        
        // Create and save the reel
        Reel reel = Reel.builder()
                .userId(userId)
                .title(title)
                .description(description)
                .videoUrl(videoUrl)
                .thumbnailUrl(thumbnailUrl)
                .durationMs(0) // This would be calculated from the video
                .likesCount(0)
                .viewsCount(0)
                .isPrivate(privacyStatus.equalsIgnoreCase("private"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        
        Reel savedReel = reelRepository.save(reel);
        return convertToDTO(savedReel, user.getUsername());
    }
    
    public List<ReelDTO> getAllReels() {
        // Get all public reels
        return reelRepository.findByIsPrivateFalseOrderByCreatedAtDesc().stream()
                .map(reel -> {
                    String username = userRepository.findById(reel.getUserId())
                            .map(User::getUsername)
                            .orElse("Unknown");
                    return convertToDTO(reel, username);
                })
                .collect(Collectors.toList());
    }
    
    public List<ReelDTO> getPublicReels() {
        // This method returns the same as getAllReels for now
        // In the future, it could be extended to include additional filtering or sorting
        return getAllReels();
    }
    
    public ReelDTO getReel(Long id) {
        Reel reel = reelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reel not found with id: " + id));
        
        String username = userRepository.findById(reel.getUserId())
                .map(User::getUsername)
                .orElse("Unknown");
        
        return convertToDTO(reel, username);
    }
    
    public ReelDTO updateReel(Long id, ReelDTO reelDTO) {
        Reel reel = reelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reel not found with id: " + id));
        
        // Update fields
        reel.setTitle(reelDTO.getTitle());
        reel.setDescription(reelDTO.getDescription());
        reel.setVideoUrl(reelDTO.getVideoUrl());
        reel.setThumbnailUrl(reelDTO.getThumbnailUrl());
        reel.setDurationMs(reelDTO.getDurationMs());
        reel.setPrivate(reelDTO.isPrivate());
        reel.setUpdatedAt(Instant.now());
        
        // Save updated reel
        Reel updatedReel = reelRepository.save(reel);
        
        String username = userRepository.findById(updatedReel.getUserId())
                .map(User::getUsername)
                .orElse("Unknown");
        
        return convertToDTO(updatedReel, username);
    }
    
    public void deleteReel(Long id) {
        if (!reelRepository.existsById(id)) {
            throw new EntityNotFoundException("Reel not found with id: " + id);
        }
        reelRepository.deleteById(id);
    }
    
    @Transactional
    public void incrementLikesCount(Long id) {
        if (!reelRepository.existsById(id)) {
            throw new EntityNotFoundException("Reel not found with id: " + id);
        }
        reelRepository.incrementLikesCount(id);
    }
    
    @Transactional
    public void incrementViewsCount(Long id) {
        if (!reelRepository.existsById(id)) {
            throw new EntityNotFoundException("Reel not found with id: " + id);
        }
        reelRepository.incrementViewsCount(id);
    }
    
    private ReelDTO convertToDTO(Reel reel, String username) {
        return ReelDTO.builder()
                .id(reel.getId())
                .userId(reel.getUserId())
                .username(username)
                .title(reel.getTitle())
                .description(reel.getDescription())
                .videoUrl(reel.getVideoUrl())
                .thumbnailUrl(reel.getThumbnailUrl())
                .durationMs(reel.getDurationMs())
                .likesCount(reel.getLikesCount())
                .viewsCount(reel.getViewsCount())
                .isPrivate(reel.isPrivate())
                .createdAt(reel.getCreatedAt())
                .updatedAt(reel.getUpdatedAt())
                .build();
    }
}