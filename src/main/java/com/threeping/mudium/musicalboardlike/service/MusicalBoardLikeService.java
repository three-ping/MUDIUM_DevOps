package com.threeping.mudium.musicalboardlike.service;

public interface MusicalBoardLikeService {
    Boolean findExistingLike(Long postId, Long userId);

    void createBoardLike(Long postId, Long userId);

    void deleteBoardLike(Long postId, Long userId);
}
