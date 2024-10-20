package com.threeping.mudium.boardcomment.controller;

import com.threeping.mudium.boardcomment.dto.BoardCommentDTO;
import com.threeping.mudium.boardcomment.service.BoardCommentService;
import com.threeping.mudium.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;



@RestController
@RequestMapping("/api/board-comment")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Autowired
    BoardCommentController(BoardCommentService boardCommentService){
        this.boardCommentService = boardCommentService;
    }

    @GetMapping("{boardId}")
    private ResponseDTO<?> viewBoardComment(@PathVariable Long boardId,
                                            Pageable pageable){
        Page<BoardCommentDTO> boardCommentDTOList =
                boardCommentService.viewBoardComment(boardId,pageable);

        return ResponseDTO.ok(boardCommentDTOList);
    }

    @PostMapping("{boardId}")
    private ResponseDTO<?> createBoardComment(@PathVariable Long boardId,
                                              @RequestBody BoardCommentDTO boardCommentDTO){
        boardCommentDTO.setBoardId(boardId);
        boardCommentService.createBoardComment(boardCommentDTO);

        return ResponseDTO.ok(null);
    }

    @PutMapping("{boardCommentId}")
    private ResponseDTO<?> updateBoardComment(@PathVariable Long boardCommentId,
                                              @RequestBody BoardCommentDTO boardCommentDTO){
        boardCommentDTO.setBoardCommentId(boardCommentId);
        boardCommentService.updateBoardComment(boardCommentDTO);

        return ResponseDTO.ok(null);
    }

    @DeleteMapping("{boardCommentId}")
    private ResponseDTO<?> deleteBoardComment(@PathVariable Long boardCommentId) {
        boardCommentService.deleteBoardComment(boardCommentId);

        return ResponseDTO.ok(null);
    }

}
