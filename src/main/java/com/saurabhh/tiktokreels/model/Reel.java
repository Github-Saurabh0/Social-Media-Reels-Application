package com.saurabhh.tiktokreels.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reels")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Long durationMs;
    private Integer likesCount = 0;
    private Integer viewsCount = 0;
    private boolean isPrivate = false;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}