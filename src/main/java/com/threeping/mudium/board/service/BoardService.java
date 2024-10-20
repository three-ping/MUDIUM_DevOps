package com.threeping.mudium.board.service;

import com.threeping.mudium.board.aggregate.enumerate.SearchType;
import com.threeping.mudium.board.dto.BoardDetailDTO;
import com.threeping.mudium.board.dto.BoardListDTO;
import com.threeping.mudium.board.dto.RegistBoardDTO;
import com.threeping.mudium.board.dto.UpdateBoardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    Page<BoardListDTO> viewBoardList(Pageable pageable);

    BoardDetailDTO viewBoard(Long boardId);

    void createBoard(RegistBoardDTO registBoardDTO);

    void updateBoard(UpdateBoardDTO updateBoardDTO);

    void deleteBoard(UpdateBoardDTO updateBoardDTO);

    Page<BoardListDTO> viewSearchedBoardList(Pageable pageable, SearchType searchType, String searchQuery);

    void plusBoardViewCount(Long boardId);
}
