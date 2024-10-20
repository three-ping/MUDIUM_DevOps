package com.threeping.mudium.boardreply.service;

import com.threeping.mudium.boardreply.dto.BoardReplyDTO;

import java.util.List;

public interface BoardReplyService {
    List<BoardReplyDTO> viewBoardReply(Long boardCommentId);

    void createBoardReply(BoardReplyDTO boardReplyDTO);

    void updateBoardReply(BoardReplyDTO boardReplyDTO);

    void deleteBoardReply(Long boardReplyId);
}
