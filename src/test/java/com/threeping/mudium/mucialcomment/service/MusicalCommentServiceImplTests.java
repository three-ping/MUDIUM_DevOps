package com.threeping.mudium.mucialcomment.service;

import com.threeping.mudium.mucialcomment.dto.MusicalCommentDTO;
import com.threeping.mudium.musicalboard.dto.MusicalPostDTO;
import com.threeping.mudium.musicalboard.dto.MusicalPostListDTO;
import com.threeping.mudium.musicalboard.repository.MusicalBoardRepository;
import com.threeping.mudium.musicalboard.service.MusicalBoardService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class MusicalCommentServiceImplTests {

    private final MusicalCommentService musicalCommentService;
    private final MusicalBoardService musicalBoardService;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    public MusicalCommentServiceImplTests(MusicalCommentService musicalCommentService,
                                          MusicalBoardService musicalBoardService) {
        this.musicalCommentService = musicalCommentService;
        this.musicalBoardService = musicalBoardService;
    }

    @DisplayName("특정 게시글의 모든 댓글을 조회한다.")
    @Test
    void searchComments() {
        // given
        Long musicalPostId = 32L;

        // when
        List<MusicalCommentDTO> dtoList = musicalCommentService.findComment(musicalPostId);

        // then
        assertNotNull(dtoList, "조회된 댓글 리스트는 null이 아니다.");
        assertTrue(!dtoList.isEmpty(), "조회된 리스트는 비어있지 않다.");
    }

    @DisplayName("특정 게시글에 댓글을 작성하면 댓글 수가 증가한다.")
    @Test
    void createCommentIncreasesCommentCount() {
        // Given
        Long userId = 1L;
        Long musicalId = 1L;
        MusicalPostDTO postDTO = new MusicalPostDTO();
        postDTO.setTitle("제목");
        postDTO.setContent("내용");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        musicalBoardService.createPost(musicalId, userId, postDTO);

        Page<MusicalPostListDTO> list = musicalBoardService.findAllPost(musicalId, pageable);
        int size = list.getContent().size();
        Long postId = list.getContent().get(size - 1).getPostId();

        MusicalCommentDTO commentDTO = new MusicalCommentDTO();
        commentDTO.setContent("댓글 댓글 댓글");
        commentDTO.setPostId(postId);

        MusicalPostDTO beforePostDTO = musicalBoardService.findPost(postId);
        Long commentCountBefore = beforePostDTO.getCommentCount();

        // When
        musicalCommentService.createComment(userId, commentDTO);
        entityManager.flush();
        entityManager.clear();

        // Then
        MusicalPostDTO afterPostDTO = musicalBoardService.findPost(postId);
        assertEquals(afterPostDTO.getCommentCount(), commentCountBefore + 1,"댓글이 작성된 후, 글의 댓글 수는 1 증가한다.");
    }

    @DisplayName("특정 게시글에 댓글을 수정한다.")
    @Test
    void updateComment() {
        // Given
        Long userId = 1L;
        Long musicalId = 1L;
        MusicalPostDTO postDTO = new MusicalPostDTO();
        postDTO.setTitle("제목");
        postDTO.setContent("내용");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        musicalBoardService.createPost(musicalId, userId, postDTO);

        Page<MusicalPostListDTO> list = musicalBoardService.findAllPost(musicalId, pageable);
        Long postId = list.getContent().get(0).getPostId();

        MusicalCommentDTO commentDTO = new MusicalCommentDTO();
        commentDTO.setContent("댓글 댓글 댓글");
        commentDTO.setPostId(postId);
        musicalCommentService.createComment(userId, commentDTO);

        String content = "수정할 내용";
        MusicalCommentDTO beforeCommentDTO = musicalCommentService.findComment(postId).get(0);
        beforeCommentDTO.setContent(content);

        // When
        musicalCommentService.updateComment(userId, beforeCommentDTO);
        entityManager.flush();
        entityManager.clear();

        // Then
        MusicalCommentDTO updatedCommentDTO = musicalCommentService.findComment(postId).get(0);
        assertEquals(updatedCommentDTO.getContent(), content,"업데이트된 댓글의 내용 일치 확인");
        assertEquals(updatedCommentDTO.getPostId(), postId, "업데이트된 댓글의 글 번호 확인");
        assertEquals(updatedCommentDTO.getCommentId(), beforeCommentDTO.getCommentId(),"업데이트된 댓글의 번호 확인");
        assertEquals(updatedCommentDTO.getNickName(), beforeCommentDTO.getNickName(), "업데이트된 댓글 작성자 확인");
    }

    @DisplayName("특정 게시글에 댓글을 삭제한다.")
    @Test
    void deleteComment() {
        // Given
        Long userId = 1L;
        Long musicalId = 1L;
        MusicalPostDTO postDTO = new MusicalPostDTO();
        postDTO.setTitle("제목");
        postDTO.setContent("내용");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        musicalBoardService.createPost(musicalId, userId, postDTO);

        Page<MusicalPostListDTO> list = musicalBoardService.findAllPost(musicalId, pageable);
        Long postId = list.getContent().get(0).getPostId();

        MusicalCommentDTO commentDTO = new MusicalCommentDTO();
        commentDTO.setContent("댓글 댓글 댓글");
        commentDTO.setPostId(postId);
        musicalCommentService.createComment(userId, commentDTO);
        MusicalCommentDTO existingCommentDTO = musicalCommentService.findComment(postId).get(0);

        // When
        musicalCommentService.deleteComment(userId, existingCommentDTO);
        entityManager.flush();
        entityManager.clear();

        // Then
        MusicalCommentDTO updatedCommentDTO = musicalCommentService.findComment(postId).get(0);
        assertEquals(updatedCommentDTO.getContent(), "삭제된 댓글입니다.","삭제된 댓글은 '삭제된 댓글입니다.'로 표시된다.");
    }
}