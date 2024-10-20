package com.threeping.mudium.boardlike.controller;

import com.threeping.mudium.boardlike.service.BoardLikeService;
import com.threeping.mudium.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/board-like")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    @Autowired
    private BoardLikeController(BoardLikeService boardLikeService){
        this.boardLikeService = boardLikeService;
    }

    @GetMapping("{boardId}/{userId}")
    private ResponseDTO<?> findBoardLike(@PathVariable Long boardId,
                                         @PathVariable Long userId){
        Boolean isExist = boardLikeService.findBoardLike(boardId,userId);
        return ResponseDTO.ok(isExist);
    }

    @PostMapping("{boardId}")
    private ResponseDTO<?> createBoardLike(@PathVariable Long boardId,
                                           @RequestBody Long userId){
        boardLikeService.createBoardLike(boardId,userId);
        return ResponseDTO.ok(null);
    }

    @DeleteMapping("{boardId}")
    private ResponseDTO<?> deleteBoardLike(@PathVariable Long boardId,
                                           @RequestBody Long userId){
        boardLikeService.deleteBoardLike(boardId,userId);
        log.info("delete?");
        return ResponseDTO.ok(null);
    }

}
