package com.saurabhh.tiktokreels.dto;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReelDTO {
    private Long id;
    private Long userId;
    private String username; // Added for display purposes
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Long durationMs;
    private Integer likesCount;
    private Integer viewsCount;
    private boolean isPrivate;
    private Instant createdAt;
    private Instant updatedAt;
}