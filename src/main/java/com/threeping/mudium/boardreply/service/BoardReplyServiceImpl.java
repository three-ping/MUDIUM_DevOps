package com.threeping.mudium.boardreply.service;

import com.threeping.mudium.board.aggregate.enumerate.ActiveStatus;
import com.threeping.mudium.boardreply.aggregate.entity.BoardReply;
import com.threeping.mudium.boardreply.dto.BoardReplyDTO;
import com.threeping.mudium.boardreply.repository.BoardReplyRepository;
import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;


@Service
public class BoardReplyServiceImpl implements BoardReplyService{

    private final BoardReplyRepository boardReplyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    BoardReplyServiceImpl(BoardReplyRepository boardReplyRepository, ModelMapper modelMapper){
        this.boardReplyRepository = boardReplyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BoardReplyDTO> viewBoardReply(Long boardCommentId) {
        List<BoardReply> boardReplyList = boardReplyRepository
                .findByBoardCommentIdAndActiveStatus(boardCommentId,ActiveStatus.ACTIVE);
        List<BoardReplyDTO> boardReplyDTOS = boardReplyList.stream()
                .map(
                        boardReply -> {
                            BoardReplyDTO boardReplyDTO = new BoardReplyDTO();
                            boardReplyDTO.setContent(boardReply.getContent());
                            boardReplyDTO.setBoardReplyId(boardReply.getBoardReplyId());
                            boardReplyDTO.setUserId(boardReply.getUser().getUserId());
                            boardReplyDTO.setNickname(boardReply.getUser().getNickname());
                            boardReplyDTO.setCreatedAt(boardReply.getCreatedAt());
                            boardReplyDTO.setUpdatedAt(boardReply.getUpdatedAt());
                            boardReplyDTO.setBoardCommentId(boardReply.getBoardCommentId());
                            return boardReplyDTO;
                        }
                )
                .toList();

        return boardReplyDTOS;
    }

    @Override
    public void createBoardReply(BoardReplyDTO boardReplyDTO) {
        BoardReply boardReply = new BoardReply();
        UserEntity user = new UserEntity();
        user.setUserId(boardReplyDTO.getUserId());
        boardReply.setBoardCommentId(boardReplyDTO.getBoardCommentId());
        boardReply.setContent(boardReplyDTO.getContent());
        boardReply.setUser(user);
        boardReply.setActiveStatus(ActiveStatus.ACTIVE);
        boardReply.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        boardReplyRepository.save(boardReply);
    }

    @Override
    public void updateBoardReply(BoardReplyDTO boardReplyDTO) {
        BoardReply boardReply  = boardReplyRepository.findById(boardReplyDTO.getBoardReplyId())
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_BOARD_REPLY));
        boardReply.setContent(boardReplyDTO.getContent());
        boardReply.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boardReplyRepository.save(boardReply);
    }

    @Override
    public void deleteBoardReply(Long boardReplyId) {
        BoardReply boardReply = boardReplyRepository.findById(boardReplyId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_BOARD_REPLY));
        boardReply.setActiveStatus(ActiveStatus.INACTIVE);
        boardReply.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boardReplyRepository.save(boardReply);
    }
}
