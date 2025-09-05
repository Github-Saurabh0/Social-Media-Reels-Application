package com.saurabhh.tiktokreels.repository;

import com.saurabhh.tiktokreels.model.Reel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReelRepository extends JpaRepository<Reel, Long> {
    List<Reel> findByUserId(Long userId);
    List<Reel> findByIsPrivateFalseOrderByCreatedAtDesc();
    
    @Modifying
    @Query("UPDATE Reel r SET r.likesCount = r.likesCount + 1 WHERE r.id = :reelId")
    void incrementLikesCount(Long reelId);
    
    @Modifying
    @Query("UPDATE Reel r SET r.viewsCount = r.viewsCount + 1 WHERE r.id = :reelId")
    void incrementViewsCount(Long reelId);
}