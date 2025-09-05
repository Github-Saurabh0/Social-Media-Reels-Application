package com.saurabhh.tiktokreels.controller;

import com.saurabhh.tiktokreels.dto.ReelDTO;
import com.saurabhh.tiktokreels.service.ReelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reels")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReelController {
    private final ReelService reelService;

    @PostMapping
    public ResponseEntity<ReelDTO> createReel(@RequestBody ReelDTO reelDTO) {
        return ResponseEntity.ok(reelService.createReel(reelDTO));
    }

    @GetMapping
    public ResponseEntity<List<ReelDTO>> getAllReels() {
        return ResponseEntity.ok(reelService.getAllReels());
    }
    
    @GetMapping("/public")
    public ResponseEntity<List<ReelDTO>> getPublicReels() {
        return ResponseEntity.ok(reelService.getPublicReels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReelDTO> getReel(@PathVariable Long id) {
        return ResponseEntity.ok(reelService.getReel(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReelDTO> updateReel(@PathVariable Long id, @RequestBody ReelDTO reelDTO) {
        return ResponseEntity.ok(reelService.updateReel(id, reelDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReel(@PathVariable Long id) {
        reelService.deleteReel(id);
        return ResponseEntity.ok("Reel deleted successfully");
    }
    
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeReel(@PathVariable Long id) {
        reelService.incrementLikesCount(id);
        return ResponseEntity.ok("Reel liked successfully");
    }
    
    @PostMapping("/{id}/view")
    public ResponseEntity<String> viewReel(@PathVariable Long id) {
        reelService.incrementViewsCount(id);
        return ResponseEntity.ok("View count incremented successfully");
    }
}