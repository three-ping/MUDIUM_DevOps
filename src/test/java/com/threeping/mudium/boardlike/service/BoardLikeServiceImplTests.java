package com.threeping.mudium.boardlike.service;

import com.threeping.mudium.boardlike.aggregate.entity.BoardLike;
import com.threeping.mudium.boardlike.aggregate.entity.BoardLikePK;
import com.threeping.mudium.boardlike.repository.BoardLikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardLikeServiceImplTests {

    private final BoardLikeService boardLikeService;
    private final BoardLikeRepository boardLikeRepository;

    @Autowired
    BoardLikeServiceImplTests(BoardLikeService boardLikeService,
                              BoardLikeRepository boardLikeRepository){
        this.boardLikeService = boardLikeService;
        this.boardLikeRepository = boardLikeRepository;
    }


    @Test
    @DisplayName("게시글 좋아요를 찾는다.")
    void findBoardLikeTest() {
        Long boardId = -1L;
        Long userId = -1L;

        boardLikeService.createBoardLike(boardId, userId);
        Boolean result = boardLikeService.findBoardLike(boardId, userId);
        assertTrue(result, "좋아요가 존재해야 한다.");

        Boolean nonExistentResult = boardLikeService.findBoardLike(-2L, -2L);
        assertFalse(nonExistentResult, "존재하지 않는 좋아요는 false여야 한다.");
    }

    @Test
    @DisplayName("게시글 좋아요를 생성한다.")
    void createBoardLikeTest() {
        Long boardId = -1L;
        Long userId = -1L;

        // 게시글 좋아요를 생성합니다.
        boardLikeService.createBoardLike(boardId, userId);

        BoardLikePK boardLikePK = new BoardLikePK(boardId, userId);
        BoardLike boardLike = boardLikeRepository.findById(boardLikePK).orElse(null);
        assertNotNull(boardLike, "좋아요가 저장되어 있어야 한다.");
    }

    @Test
    @DisplayName("게시글 좋아요를 삭제한다.")
    void deleteBoardLikeTest() {
        Long boardId = -1L;
        Long userId = -1L;

        boardLikeService.createBoardLike(boardId, userId);
        boardLikeService.deleteBoardLike(boardId, userId);

        BoardLikePK boardLikePK = new BoardLikePK(boardId, userId);
        BoardLike boardLike = boardLikeRepository.findById(boardLikePK).orElse(null);
        assertNull(boardLike, "좋아요가 삭제되어 있어야 한다.");
    }
}
