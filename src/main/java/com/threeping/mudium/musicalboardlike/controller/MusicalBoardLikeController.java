package com.threeping.mudium.musicalboardlike.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.musicalboardlike.service.MusicalBoardLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/api/musical-board-like")
public class MusicalBoardLikeController {

    private final MusicalBoardLikeService musicalBoardLikeService;

    @Autowired
    public MusicalBoardLikeController(MusicalBoardLikeService musicalBoardLikeService) {
        this.musicalBoardLikeService = musicalBoardLikeService;
    }

    @GetMapping("/{postId}/{userId}")
    public ResponseDTO<?> findBoardLike(@PathVariable Long postId, @PathVariable Long userId) {
        Boolean check = musicalBoardLikeService.findExistingLike(postId, userId);

        return ResponseDTO.ok(check);
    }

    @PostMapping("/{postId}")
    public ResponseDTO<?> createBoardLike(@PathVariable Long postId, @RequestBody Long userId) {
        musicalBoardLikeService.createBoardLike(postId, userId);

        return ResponseDTO.ok(null);
    }

    @DeleteMapping("/{postId}")
    public ResponseDTO<?> deleteBoardLike(@PathVariable Long postId, @PathVariable Long userId) {
        musicalBoardLikeService.deleteBoardLike(postId, userId);

        return ResponseDTO.ok(null);
    }
}
