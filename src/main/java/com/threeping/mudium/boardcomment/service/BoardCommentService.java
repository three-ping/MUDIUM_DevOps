package com.threeping.mudium.boardcomment.service;

import com.threeping.mudium.boardcomment.dto.BoardCommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCommentService {
    Page<BoardCommentDTO> viewBoardComment(Long boardId, Pageable pageable);

    void createBoardComment(BoardCommentDTO boardCommentDTO);

    void updateBoardComment(BoardCommentDTO boardCommentDTO);

    void deleteBoardComment(Long boardCommentDTO);
}
