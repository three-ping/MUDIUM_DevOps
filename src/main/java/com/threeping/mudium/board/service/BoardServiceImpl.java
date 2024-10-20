package com.threeping.mudium.board.service;

import com.threeping.mudium.board.aggregate.enumerate.ActiveStatus;
import com.threeping.mudium.board.aggregate.entity.Board;
import com.threeping.mudium.board.aggregate.enumerate.SearchType;
import com.threeping.mudium.board.dto.BoardDetailDTO;
import com.threeping.mudium.board.dto.BoardListDTO;
import com.threeping.mudium.board.dto.RegistBoardDTO;
import com.threeping.mudium.board.dto.UpdateBoardDTO;
import com.threeping.mudium.board.repository.BoardRepository;
import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import com.threeping.mudium.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Autowired
    BoardServiceImpl(BoardRepository boardRepository,
                     ModelMapper modelMapper, UserRepository userRepository){
        this.boardRepository = boardRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Page<BoardListDTO> viewBoardList(Pageable pageable) {
        int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
        int pageSize = pageable.getPageSize();
        Sort pageSort = Sort.by("createdAt").descending();
        Pageable boardPageable = PageRequest.of(pageNumber,pageSize,pageSort);

        Page<Board> boards = boardRepository.findByActiveStatus(ActiveStatus.ACTIVE,boardPageable);

        List<BoardListDTO> boardListDTOList = boards.stream()
                .map(board -> {
                    BoardListDTO boardListDTO = modelMapper.map(board, BoardListDTO.class);
                    boardListDTO.setNickname(board.getUser().getNickname());
                    return boardListDTO;
                })
                .collect(Collectors.toList());
        Page<BoardListDTO> boardListDTOs = new PageImpl<>(boardListDTOList,boardPageable,boards.getTotalElements());

        return boardListDTOs;
    }

    @Override
    @Transactional
    public BoardDetailDTO viewBoard(Long boardId) {

        Board board = boardRepository.findByActiveStatusAndBoardId(ActiveStatus.ACTIVE,boardId).orElseThrow(()->new CommonException(ErrorCode.INVALID_BOARD_ID));
        BoardDetailDTO boardDetailDTO = modelMapper.map(board, BoardDetailDTO.class);
        boardDetailDTO.setNickname(board.getUser().getNickname());

        return boardDetailDTO;
    }

    @Override
    @Transactional
    public void createBoard(RegistBoardDTO registBoardDTO) {
        Board board = new Board();
        board.setTitle(registBoardDTO.getTitle());
        board.setContent(registBoardDTO.getContent());
        board.setActiveStatus(ActiveStatus.ACTIVE);
        board.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        board.setBoardLike(0L);
        board.setViewCount(0L);
        board.setComments(0L);
        UserEntity user = userRepository.findById(registBoardDTO.getUserId()).orElseThrow(()->
             new CommonException(ErrorCode.NOT_FOUND_USER)
        );
        board.setUser(user);
        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void updateBoard(UpdateBoardDTO updateBoardDTO) {
        Board board = boardRepository.findByActiveStatusAndBoardIdAndUser_UserId(
                ActiveStatus.ACTIVE,
                updateBoardDTO.getBoardId(),
                updateBoardDTO.getUserId()
                ).orElseThrow(
                ()-> new CommonException(ErrorCode.INVALID_BOARD_USER_ID)
        );
        if(!updateBoardDTO.getTitle().isEmpty()) {
            board.setTitle(updateBoardDTO.getTitle());
        }

        if(!updateBoardDTO.getContent().isEmpty()) {
            board.setContent(updateBoardDTO.getContent());
        }

        board.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void deleteBoard(UpdateBoardDTO updateBoardDTO) {
        Board board = boardRepository.findByActiveStatusAndBoardIdAndUser_UserId(
                ActiveStatus.ACTIVE,
                updateBoardDTO.getBoardId(),
                updateBoardDTO.getUserId()
        ).orElseThrow(
                ()->
                     new CommonException(ErrorCode.INVALID_BOARD_USER_ID)
        );
        board.setActiveStatus(ActiveStatus.INACTIVE);
        board.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        boardRepository.save(board);
    }


    public Page<BoardListDTO> searchBoardsByUser_NickName(String nickname, Pageable pageable) {
        return boardRepository.findByUser_NicknameContaining(nickname, pageable).map(this::convertToDTO);
    }

    public Page<BoardListDTO> searchBoardsByTitle(String title, Pageable pageable) {
        return boardRepository.findByTitleContaining(title, pageable).map(this::convertToDTO);
    }

    public Page<BoardListDTO> searchBoardsByContent(String content, Pageable pageable) {
        return boardRepository.findByContentContaining(content, pageable).map(this::convertToDTO);
    }

    @Override
    @Transactional
    public Page<BoardListDTO> viewSearchedBoardList(Pageable pageable, SearchType searchType, String searchQuery) {
        int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
        int pageSize = pageable.getPageSize();
        Sort pageSort = Sort.by("createdAt").descending();
        Pageable boardPageable = PageRequest.of(pageNumber,pageSize,pageSort);
        if (searchType.equals(SearchType.NICKNAME)) {
            return searchBoardsByUser_NickName(searchQuery,boardPageable);
        } else if (searchType.equals(SearchType.TITLE)){
            return searchBoardsByTitle(searchQuery,boardPageable);
        } else if (searchType.equals(SearchType.CONTENT)) {
            return searchBoardsByContent(searchQuery,boardPageable);
        }
        return null;
    }

    @Override
    @Transactional
    public void plusBoardViewCount(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new CommonException(ErrorCode.INVALID_BOARD_ID));
        board.setViewCount(board.getViewCount()+1);
        boardRepository.save(board);
    }

    private BoardListDTO convertToDTO(Board board) {
        BoardListDTO boardListDTO = new BoardListDTO();
        boardListDTO.setTitle(board.getTitle());
        boardListDTO.setBoardId(board.getBoardId());
        boardListDTO.setCreatedAt(board.getCreatedAt());
        boardListDTO.setNickname(board.getUser().getNickname());
        boardListDTO.setUserId(board.getUser().getUserId());
        boardListDTO.setComments(board.getComments());
        boardListDTO.setBoardLike(board.getBoardLike());
        boardListDTO.setViewCount(board.getViewCount());
        return boardListDTO;
    }
}
