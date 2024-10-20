package com.threeping.mudium.boardcomment.service;

import com.threeping.mudium.board.aggregate.enumerate.ActiveStatus;
import com.threeping.mudium.boardcomment.aggregate.BoardComment;
import com.threeping.mudium.boardcomment.dto.BoardCommentDTO;
import com.threeping.mudium.boardcomment.repository.BoardCommentRepository;
import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
@Transactional
public class BoardCommentServiceImpl implements BoardCommentService{

    private final BoardCommentRepository boardCommentRepository;
    private final ModelMapper modelMapper;
    @Autowired
     BoardCommentServiceImpl(BoardCommentRepository boardCommentRepository,
                             ModelMapper modelMapper){
        this.boardCommentRepository = boardCommentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<BoardCommentDTO> viewBoardComment(Long boardId, Pageable pageable) {
        int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
        int pageSize = pageable.getPageSize();
        Sort pageSort = Sort.by("createdAt").ascending();
        Pageable boardCommentPageable = PageRequest.of(pageNumber,pageSize,pageSort);

        Page<BoardComment> comments = boardCommentRepository
                .findByBoardId(boardId,boardCommentPageable);

        List<BoardCommentDTO> boardCommentDTOList = comments.stream()
                .map(boardComment -> {
                    BoardCommentDTO boardCommentDTO = modelMapper.map(boardComment,BoardCommentDTO.class);
                    boardCommentDTO.setUserId(boardComment.getUser().getUserId());
                    boardCommentDTO.setNickname(boardComment.getUser().getNickname());
                    return boardCommentDTO;
                })
                .toList();

        Page<BoardCommentDTO> boardCommentPage =
                new PageImpl<>(boardCommentDTOList,boardCommentPageable,comments.getTotalElements());



        return boardCommentPage;
    }

    @Override
    public void createBoardComment(BoardCommentDTO boardCommentDTO) {
        UserEntity user = new UserEntity();
        user.setUserId(boardCommentDTO.getUserId());
        BoardComment boardComment = new BoardComment();
        boardComment.setBoardId(boardCommentDTO.getBoardId());
        boardComment.setUser(user);
        boardComment.setActiveStatus(ActiveStatus.ACTIVE);
        boardComment.setContent(boardCommentDTO.getContent());
        boardComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        boardCommentRepository.save(boardComment);
    }

    @Override
    public void updateBoardComment(BoardCommentDTO boardCommentDTO) {
        BoardComment boardComment = boardCommentRepository.findById(boardCommentDTO.getBoardCommentId())
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_BOARD_COMMENT));

        boardComment.setContent(boardCommentDTO.getContent());
        boardComment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boardCommentRepository.save(boardComment);
    }

    @Override
    public void deleteBoardComment(Long boardCommentId) {
        BoardComment boardComment = boardCommentRepository.findById(boardCommentId)
                .orElseThrow(()->new CommonException(ErrorCode.NOT_FOUND_BOARD_COMMENT));

        boardComment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boardComment.setActiveStatus(ActiveStatus.INACTIVE);
        boardCommentRepository.save(boardComment);
    }
}
