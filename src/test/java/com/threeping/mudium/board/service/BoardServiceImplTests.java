package com.threeping.mudium.board.service;

import com.threeping.mudium.board.aggregate.entity.Board;
import com.threeping.mudium.board.dto.BoardDetailDTO;
import com.threeping.mudium.board.dto.BoardListDTO;
import com.threeping.mudium.board.dto.RegistBoardDTO;
import com.threeping.mudium.board.dto.UpdateBoardDTO;
import com.threeping.mudium.board.repository.BoardRepository;
import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.board.aggregate.enumerate.ActiveStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BoardServiceImplTests {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @Autowired
    BoardServiceImplTests(BoardService boardService,
                          BoardRepository boardRepository){
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }

    @DisplayName("게시글 리스트를 페이지로 조회한다.")
    @Test
    void boardPageViewTest(){
        Pageable pageable = PageRequest.of(1,10, Sort.by("createdAt").descending());

        Page<BoardListDTO> boardDTOPage = boardService.viewBoardList(pageable);

        assertNotNull(boardDTOPage, "조회된 페이지는 null이 아니다.");
        assertTrue(boardDTOPage.hasContent(), "조회된 페이지는 내용이 있다.");
        assertEquals(10, boardDTOPage.getSize(), "페이지 크기는 10이다.");

    }

    @DisplayName("게시글 상세 정보를 조회한다.")
    @Test
    void boardDetailViewTest(){
        String title = "테스트 제목";
        String content = "테스트 내용";
        Long userId = 1L;
        RegistBoardDTO registBoardDTO = new RegistBoardDTO(userId,title,content);
        boardService.createBoard(registBoardDTO);
        Board savedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);
        Long boardId = savedBoard.getBoardId();
        Long invalidBoardId = -1L;

        BoardDetailDTO firstBoardDetail = boardService.viewBoard(boardId);

        assertNotNull(firstBoardDetail,"조회된 게시글은 null이 아니다.");
        assertEquals(boardId,firstBoardDetail.getId(),"조회된 게시글 ID는 요청된 ID와 일치한다.");
        Exception exception = assertThrows(CommonException.class,
                ()->boardService.viewBoard(invalidBoardId));
        assertEquals("잘못된 자유게시글 번호입니다.",exception.getMessage());

    }

    @DisplayName("게시글을 등록한다.")
    @Test
    void boardCreateTest(){
        String title = "테스트 제목";
        String content = "테스트 내용";
        Long userId = 1L;
        RegistBoardDTO registBoardDTO = new RegistBoardDTO(userId,title,content);

        boardService.createBoard(registBoardDTO);

        Board savedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);

        assertEquals(title, savedBoard.getTitle());
        assertEquals(content, savedBoard.getContent());
        assertEquals(0L, savedBoard.getBoardLike());
        assertEquals(0L, savedBoard.getViewCount());
        assertEquals(ActiveStatus.ACTIVE, savedBoard.getActiveStatus());
        assertNotNull(savedBoard.getCreatedAt());
        assertEquals(userId, savedBoard.getUser().getUserId());
    }

    @Test
    @DisplayName("게시글을 수정한다.")
    void boardUpdateTest(){
        String title = "테스트 제목";
        String content = "테스트 내용";
        Long userId = 1L;
        RegistBoardDTO registBoardDTO = new RegistBoardDTO(userId,title,content);

        boardService.createBoard(registBoardDTO);

        Board savedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);

        String updatedTitle = "수정할제목";
        String updatedContent = "수정할내용";
        UpdateBoardDTO updateBoardDTO = new UpdateBoardDTO(
                updatedTitle,updatedContent,1L,savedBoard.getBoardId(),0L);
        Board updatedBoard = boardRepository.findById(savedBoard.getBoardId()).orElseThrow();
        boardService.updateBoard(updateBoardDTO);
        assertEquals(updatedTitle, updatedBoard.getTitle());
        assertEquals(updatedContent, updatedBoard.getContent());
        assertNotNull(updatedBoard.getUpdatedAt());
    }

    @Test
    @DisplayName("게시글을 삭제한다.")
    void boardDeleteTest(){
        String title = "테스트 제목";
        String content = "테스트 내용";
        Long userId = 1L;
        RegistBoardDTO registBoardDTO = new RegistBoardDTO(userId,title,content);

        boardService.createBoard(registBoardDTO);

        Board savedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);
        UpdateBoardDTO boardDTO = new UpdateBoardDTO();
        boardDTO.setBoardId(savedBoard.getBoardId());
        boardDTO.setUserId(savedBoard.getUser().getUserId());
        boardService.deleteBoard(boardDTO);

        assertEquals(savedBoard.getActiveStatus(),ActiveStatus.INACTIVE);
    }

    @Test
    @DisplayName("게시글을 조회시 조회수를 1 증가시킨다.")
    void plusBoardViewCountTest(){
        String title = "테스트 제목";
        String content = "테스트 내용";
        Long userId = 1L;
        RegistBoardDTO registBoardDTO = new RegistBoardDTO(userId,title,content);
        boardService.createBoard(registBoardDTO);
        Board savedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);
        Long oldViewCount = savedBoard.getViewCount();
        Long boardId = savedBoard.getBoardId();

        boardService.plusBoardViewCount(boardId);
        Board plusedBoard = boardRepository.findAll(Sort.by("createdAt").descending()).get(0);

        assertEquals(oldViewCount+1,plusedBoard.getViewCount(),
                "조회된 게시글의 조회수는 1 증가한다.");
    }
}