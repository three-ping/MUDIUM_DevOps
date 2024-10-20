package com.threeping.mudium.boardlike.service;

public interface BoardLikeService {
    void createBoardLike(Long boardId, Long userId);

    void deleteBoardLike(Long boardId, Long userId);

    Boolean findBoardLike(Long boardId, Long userId);
}
