package com.threeping.mudium.musicalboard.service;

import com.threeping.mudium.musicalboard.dto.MusicalPostDTO;
import com.threeping.mudium.musicalboard.dto.MusicalPostListDTO;
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
import java.util.List;

@SpringBootTest
@Transactional
class MusicalBoardServiceImplTest {

    private final MusicalBoardService musicalBoardService;

    @Autowired
    public MusicalBoardServiceImplTest(MusicalBoardService musicalBoardService) {
        this.musicalBoardService = musicalBoardService;
    }

    @DisplayName("특정 뮤지컬 게시글 전부 조회한다.")
    @Test
    void findAllPosts() {
        // given
        Long musicalBoardId = 1L;
        Pageable pageable = PageRequest.of(0, 15, Sort.by("createdAt").descending());

        // when
        Page<MusicalPostListDTO> postList = musicalBoardService.findAllPost(musicalBoardId, pageable);

        // then
        assertNotNull(postList, "게시글 리스트는 null이 아니다.");
        assertFalse(postList.isEmpty(), "게시글 리스트는 비어있지 않다.");
    }

    @DisplayName("특정 뮤지컬 게시판의 특정 게시글을 조회한다.")
    @Test
    void findPostById() {
        // given
        Long musicalBoardId = 1L;

        // when
        MusicalPostDTO postDTO = musicalBoardService.findPost(musicalBoardId);

        // then
        assertNotNull(postDTO, "조회된 게시글은 null이 아니다.");
    }

    @DisplayName("특정 뮤지컬 게시판에 게시글을 작성한다.")
    @Test
    void createPost() {
        // given
        Long musicalId = 1L;
        Long userId = 2L;
        String title = "제목";
        String content = "내용";

        // when
        MusicalPostDTO musicalPostDTO = new MusicalPostDTO();
        musicalPostDTO.setTitle(title);
        musicalPostDTO.setContent(content);

        // then
        assertDoesNotThrow(() -> musicalBoardService.createPost(musicalId, userId, musicalPostDTO));
    }


    @DisplayName("특정 게시글을 수정한다.")
    @Test
    void updatePost() {
        // given
        Long musicalId = 2L;
        Long userId = 2L;
        String content = "수정된 내용";
        String title = "수정된 제목";


        MusicalPostDTO musicalPostDTO = new MusicalPostDTO();
        musicalPostDTO.setTitle(title);
        musicalPostDTO.setContent(content);

        // then
        assertDoesNotThrow(() -> musicalBoardService.updatePost(musicalId, userId, musicalPostDTO));
    }

    @DisplayName("특정 게시글을 삭제한다.")
    @Test
    void deletePost() {
        // given
        Long musicalId = 2L;
        Long userId = 2L;

        // then
        assertDoesNotThrow(() -> musicalBoardService.deletePost(musicalId, userId));
    }
}