package com.threeping.mudium.board.controller;

import com.threeping.mudium.board.aggregate.enumerate.SearchType;
import com.threeping.mudium.board.dto.BoardDetailDTO;
import com.threeping.mudium.board.dto.BoardListDTO;
import com.threeping.mudium.board.dto.RegistBoardDTO;
import com.threeping.mudium.board.dto.UpdateBoardDTO;
import com.threeping.mudium.board.service.BoardService;
import com.threeping.mudium.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService  boardService;

    @Autowired
    private BoardController(BoardService boardService){
        this.boardService = boardService;
    }

    @GetMapping("")
    public ResponseDTO<?> viewBoardPage(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchQuery,
            Pageable pageable) {

        Page<BoardListDTO> boardPage;

        if (searchType != null && searchQuery != null && !searchQuery.trim().isEmpty()) {
            boardPage = boardService.viewSearchedBoardList(pageable,searchType,searchQuery);
        } else {
            boardPage = boardService.viewBoardList(pageable);
        }

        return ResponseDTO.ok(boardPage);
    }

    @GetMapping("{boardId}")
    private ResponseDTO<?> viewDetailBoard(@PathVariable Long boardId){
        BoardDetailDTO boardDetail = boardService.viewBoard(boardId);
        return ResponseDTO.ok(boardDetail);
    }

    @PostMapping("")
    private ResponseDTO<?> createBoard(@RequestBody RegistBoardDTO registBoardDTO){
        boardService.createBoard(registBoardDTO);
        return ResponseDTO.ok(null);
    }

    @PutMapping("{boardId}")
    private ResponseDTO<?> updateBoard(@PathVariable Long boardId,
                                       @RequestBody UpdateBoardDTO updateBoardDTO){
        updateBoardDTO.setBoardId(boardId);
        boardService.updateBoard(updateBoardDTO);
        return ResponseDTO.ok(null);
    }

    @DeleteMapping("{boardId}/{userId}")
    private ResponseDTO<?> deleteBoard(@PathVariable Long boardId,
                                       @PathVariable Long userId){
        UpdateBoardDTO updateBoardDTO = new UpdateBoardDTO();
        updateBoardDTO.setBoardId(boardId);
        updateBoardDTO.setUserId(userId);
        boardService.deleteBoard(updateBoardDTO);
        return ResponseDTO.ok(null);
    }

    @PutMapping("{boardId}/count")
    private ResponseDTO<?> plusBoardViewCount(@PathVariable Long boardId){
        boardService.plusBoardViewCount(boardId);
        return ResponseDTO.ok(null);

    }
}
