package com.threeping.mudium.musicalreply.service;

import com.threeping.mudium.mucialcomment.dto.MusicalCommentDTO;
import com.threeping.mudium.mucialcomment.service.MusicalCommentService;
import com.threeping.mudium.musicalboard.dto.MusicalPostDTO;
import com.threeping.mudium.musicalboard.dto.MusicalPostListDTO;
import com.threeping.mudium.musicalboard.service.MusicalBoardService;
import com.threeping.mudium.musicalreply.dto.MusicalReplyDTO;
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
class MusicalReplyServiceImplTests {

    private final MusicalReplyService musicalReplyService;
    private final MusicalCommentService musicalCommentService;
    private final MusicalBoardService musicalBoardService;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    public MusicalReplyServiceImplTests(MusicalReplyService musicalReplyService,
                                        MusicalCommentService musicalCommentService,
                                        MusicalBoardService musicalBoardService) {
        this.musicalReplyService = musicalReplyService;
        this.musicalCommentService = musicalCommentService;
        this.musicalBoardService = musicalBoardService;
    }

    @DisplayName("특정 댓글의 모든 대댓글을 조회한다.")
    @Test
    void searchReplies() {
        // Given
        Long commentId = 51L;

        // When
        List<MusicalReplyDTO> list = musicalReplyService.findAllReply(commentId);
        // Then
        assertNotNull(list, "조회된 대댓글 list는 null이 아니다.");
        assertTrue(list.get(0).getCommentId() == commentId, "조회된 대댓글의 댓글 Id 일치 확인");
    }

    @DisplayName("특정 댓글에 대댓글을 작성한다.")
    @Test
    void createReplies() {
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
        MusicalCommentDTO savedComment = musicalCommentService.findComment(postId).get(0);
        Long commentId = savedComment.getCommentId();

        // When
        MusicalReplyDTO replyDTO = new MusicalReplyDTO();
        String content = "대대대대대댓글";
        replyDTO.setCommentId(commentId);
        replyDTO.setContent(content);
        musicalReplyService.createReply(userId, replyDTO);

        MusicalReplyDTO dto = musicalReplyService.findAllReply(commentId).get(0);

        // Then
        assertNotNull(dto, "생성된 대댓글 not null 확인");
        assertTrue(dto.getCommentId() == commentId, "생성된 대댓글의 댓글 Id 확인");
        assertTrue(dto.getContent().equals(content), "작성된 대댓글 내용 확인");
        // 대댓글 trigger가 추가되면 대댓글이 생성될 경우, 게시글의 댓글 수가 증가하는지 테스트!
    }

    @DisplayName("특정 댓글에 대한 대댓글 수정")
    @Test
    void updateReply() {
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
        MusicalCommentDTO savedComment = musicalCommentService.findComment(postId).get(0);
        Long commentId = savedComment.getCommentId();

        MusicalReplyDTO replyDTO = new MusicalReplyDTO();
        String content = "대대대대대댓글";
        replyDTO.setCommentId(commentId);
        replyDTO.setContent(content);
        musicalReplyService.createReply(userId, replyDTO);

        String newContent = "수정 수정 수정";
        MusicalReplyDTO createdDTO = musicalReplyService.findAllReply(commentId).get(0);
        createdDTO.setContent(newContent);
        musicalReplyService.updateReply(userId, createdDTO);

        MusicalReplyDTO dto = musicalReplyService.findAllReply(commentId).get(0);

        // Then
        assertNotNull(dto, "수정된 대댓글 not null 확인");
        assertTrue(dto.getCommentId() == commentId, "수정된 대댓글의 댓글 Id 확인");
        assertTrue(dto.getContent().equals(newContent), "수정된 대댓글 내용 확인");
    }

    @DisplayName("특정 댓글에 대한 대댓글을 삭제한다.")
    @Test
    void deleteReply() {
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
        MusicalCommentDTO savedComment = musicalCommentService.findComment(postId).get(0);
        Long commentId = savedComment.getCommentId();

        MusicalReplyDTO replyDTO = new MusicalReplyDTO();
        String content = "대대대대대댓글";
        replyDTO.setCommentId(commentId);
        replyDTO.setContent(content);
        musicalReplyService.createReply(userId, replyDTO);
        MusicalReplyDTO createdDTO = musicalReplyService.findAllReply(commentId).get(0);

        // When
        musicalReplyService.deleteReply(userId, createdDTO);
        MusicalReplyDTO dto = musicalReplyService.findAllReply(commentId).get(0);

        // Then
        assertNotNull(dto, "삭제된 대댓글 not null 확인");
        assertTrue(dto.getCommentId() == commentId, "삭제된 대댓글의 댓글 Id 확인");
        assertTrue(dto.getContent().equals("삭제된 댓글입니다."), "삭제된 대댓글 내용 확인");
        // 삭제된 후, 게시글 리스트에서 댓글 수 표시가 -1됐는지 확인하는 테스트 추가할 예정
    }
}