package com.threeping.mudium.boardreply.service;

import com.threeping.mudium.board.aggregate.entity.Board;
import com.threeping.mudium.board.aggregate.enumerate.ActiveStatus;
import com.threeping.mudium.board.dto.RegistBoardDTO;
import com.threeping.mudium.board.repository.BoardRepository;
import com.threeping.mudium.board.service.BoardService;
import com.threeping.mudium.boardcomment.aggregate.BoardComment;
import com.threeping.mudium.boardcomment.dto.BoardCommentDTO;
import com.threeping.mudium.boardcomment.repository.BoardCommentRepository;
import com.threeping.mudium.boardcomment.service.BoardCommentService;
import com.threeping.mudium.boardreply.aggregate.entity.BoardReply;
import com.threeping.mudium.boardreply.dto.BoardReplyDTO;
import com.threeping.mudium.boardreply.repository.BoardReplyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardReplyServiceImplTests {

    private final BoardReplyService boardReplyService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final BoardCommentService boardCommentService;
    private final BoardCommentRepository boardCommentRepository;
    @Autowired
    private BoardReplyRepository boardReplyRepository;

    @Autowired
    BoardReplyServiceImplTests(BoardReplyService boardReplyService,
                               BoardService boardService,
                               BoardRepository boardRepository,
                               BoardCommentService boardCommentService,
                               BoardCommentRepository boardCommentRepository){
        this.boardReplyService = boardReplyService;
        this.boardService = boardService;
        this.boardRepository = boardRepository;
        this.boardCommentService = boardCommentService;
        this.boardCommentRepository = boardCommentRepository;
    }

    @DisplayName("자유게시글 대댓글을 조회한다.")
    @Test
    void viewBoardReplyTest(){
        String boardTitle = "테스트 제목";
        String boardContent = "테스트 내용";
        Long userId = 1L;
        RegistBoardDTO registBoardDTO = new RegistBoardDTO(userId,boardTitle,boardContent);
        boardService.createBoard(registBoardDTO);
        Board savedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);
        String commentContent = "테스트 댓글 내용";
        BoardCommentDTO boardCommentDTO = new BoardCommentDTO();
        boardCommentDTO.setBoardId(savedBoard.getBoardId());
        boardCommentDTO.setUserId(userId);
        boardCommentDTO.setContent(commentContent);
        boardCommentDTO.setActiveStatus(ActiveStatus.ACTIVE);
        boardCommentDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardCommentService.createBoardComment(boardCommentDTO);

        BoardComment boardComment = boardCommentRepository
                .findByBoardId(
                        savedBoard.getBoardId(),
                        PageRequest.of(0,
                                10,
                                Sort.by("createdAt").descending())).getContent().get(0);
        String content = "테스트 대댓글 내용";

        BoardReplyDTO boardReplyDTO = new BoardReplyDTO();
        boardReplyDTO.setBoardCommentId(boardComment.getBoardCommentId());
        boardReplyDTO.setContent(content);
        boardReplyDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardReplyDTO.setUserId(userId);
        boardReplyService.createBoardReply(boardReplyDTO);

        Long newRelyId = boardReplyService.viewBoardReply(boardComment.getBoardCommentId()).get(0).getBoardReplyId();

        List<BoardReplyDTO> boardReplyDTOs = boardReplyService.viewBoardReply(boardComment.getBoardCommentId());

        assertEquals(newRelyId,boardReplyDTOs.get(0).getBoardReplyId());
        assertEquals(content,boardReplyDTOs.get(0).getContent());

    }

    @DisplayName("자유게시글 대댓글을 작성한다.")
    @Test
    void createBoardReplyTest(){
        String boardTitle = "테스트 제목";
        String boardContent = "테스트 내용";
        Long userId = 1L;
        RegistBoardDTO registBoardDTO = new RegistBoardDTO(userId,boardTitle,boardContent);
        boardService.createBoard(registBoardDTO);
        Board savedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);
        String commentContent = "테스트 댓글 내용";
        BoardCommentDTO boardCommentDTO = new BoardCommentDTO();
        boardCommentDTO.setBoardId(savedBoard.getBoardId());
        boardCommentDTO.setUserId(userId);
        boardCommentDTO.setContent(commentContent);
        boardCommentDTO.setActiveStatus(ActiveStatus.ACTIVE);
        boardCommentDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardCommentService.createBoardComment(boardCommentDTO);

        BoardComment boardComment = boardCommentRepository
                .findByBoardId(
                        savedBoard.getBoardId(),
                        PageRequest.of(0,
                                10,
                                Sort.by("createdAt").descending())).getContent().get(0);
        String content = "테스트 대댓글 내용";

        BoardReplyDTO boardReplyDTO = new BoardReplyDTO();
        boardReplyDTO.setBoardCommentId(boardComment.getBoardCommentId());
        boardReplyDTO.setContent(content);
        boardReplyDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardReplyDTO.setUserId(userId);
        boardReplyService.createBoardReply(boardReplyDTO);

        Long newRelyId = boardReplyService.viewBoardReply(boardComment.getBoardCommentId()).get(0).getBoardReplyId();
        BoardReply boardReply = boardReplyRepository.findById(newRelyId).orElseThrow();

        assertNotNull(boardReply);
        assertEquals(content,boardReply.getContent());

    }

    @DisplayName("자유게시글 대댓글을 수정한다.")
    @Test
    void updateBoardReplyTest(){
        String boardTitle = "테스트 제목";
        String boardContent = "테스트 내용";
        Long userId = 1L;
        RegistBoardDTO registBoardDTO = new RegistBoardDTO(userId,boardTitle,boardContent);
        boardService.createBoard(registBoardDTO);
        Board savedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);
        String commentContent = "테스트 댓글 내용";
        BoardCommentDTO boardCommentDTO = new BoardCommentDTO();
        boardCommentDTO.setBoardId(savedBoard.getBoardId());
        boardCommentDTO.setUserId(userId);
        boardCommentDTO.setContent(commentContent);
        boardCommentDTO.setActiveStatus(ActiveStatus.ACTIVE);
        boardCommentDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardCommentService.createBoardComment(boardCommentDTO);

        BoardComment boardComment = boardCommentRepository
                .findByBoardId(
                        savedBoard.getBoardId(),
                        PageRequest.of(0,
                                10,
                                Sort.by("createdAt").descending())).getContent().get(0);
        String content = "테스트 대댓글 내용";

        BoardReplyDTO boardReplyDTO = new BoardReplyDTO();
        boardReplyDTO.setBoardCommentId(boardComment.getBoardCommentId());
        boardReplyDTO.setContent(content);
        boardReplyDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardReplyDTO.setUserId(userId);
        boardReplyService.createBoardReply(boardReplyDTO);

        Long newRelyId = boardReplyService.viewBoardReply(boardComment.getBoardCommentId()).get(0).getBoardReplyId();

        String updatedContent = "수정된 대댓글 내용입니다.";
        BoardReplyDTO boardReplyDTOUpdated = new BoardReplyDTO();
        boardReplyDTOUpdated.setBoardReplyId(newRelyId);
        boardReplyDTOUpdated.setContent(updatedContent);
        boardReplyService.updateBoardReply(boardReplyDTOUpdated);

        BoardReply updatedBoardReply = boardReplyRepository.findById(newRelyId).orElseThrow();

        assertEquals(updatedContent,updatedBoardReply.getContent(),"수정한 내용으로 대댓글이 저장된다.");
        assertNotNull(updatedBoardReply.getUpdatedAt(),"수정시간이 저장된다.");
    }

    @DisplayName("자유게시글 대댓글을 삭제한다.")
    @Test
    void deleteBoardReplyTest(){
        String boardTitle = "테스트 제목";
        String boardContent = "테스트 내용";
        Long userId = 1L;
        RegistBoardDTO registBoardDTO = new RegistBoardDTO(userId,boardTitle,boardContent);
        boardService.createBoard(registBoardDTO);
        Board savedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);
        String commentContent = "테스트 댓글 내용";
        BoardCommentDTO boardCommentDTO = new BoardCommentDTO();
        boardCommentDTO.setBoardId(savedBoard.getBoardId());
        boardCommentDTO.setUserId(userId);
        boardCommentDTO.setContent(commentContent);
        boardCommentDTO.setActiveStatus(ActiveStatus.ACTIVE);
        boardCommentDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardCommentService.createBoardComment(boardCommentDTO);

        BoardComment boardComment = boardCommentRepository
                .findByBoardId(
                        savedBoard.getBoardId(),
                        PageRequest.of(0,
                                10,
                                Sort.by("createdAt").descending())).getContent().get(0);
        String content = "테스트 대댓글 내용";

        BoardReplyDTO boardReplyDTO = new BoardReplyDTO();
        boardReplyDTO.setBoardCommentId(boardComment.getBoardCommentId());
        boardReplyDTO.setContent(content);
        boardReplyDTO.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardReplyDTO.setUserId(userId);
        boardReplyService.createBoardReply(boardReplyDTO);

        Long newRelyId = boardReplyService.viewBoardReply(boardComment.getBoardCommentId()).get(0).getBoardReplyId();

        boardReplyService.deleteBoardReply(newRelyId);

        BoardReply updatedBoardReply = boardReplyRepository.findById(newRelyId).orElseThrow();

        assertEquals(ActiveStatus.INACTIVE,updatedBoardReply.getActiveStatus(),"삭제한 댓글은 비활설 상태이다.");
        assertNotNull(updatedBoardReply.getUpdatedAt(),"삭제시간이 저장된다.");
    }
}