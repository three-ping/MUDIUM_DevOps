package com.threeping.mudium.reviewcomment.service;

import com.threeping.mudium.reviewcomment.dto.ReviewCommentDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
class ReviewCommentServiceImplTests {

    private ReviewCommentService reviewCommentService;

    @Autowired
    public void setReviewCommentService(ReviewCommentService reviewCommentService) {
        this.reviewCommentService = reviewCommentService;
    }

    @DisplayName("리뷰에 달린 모든 댓글 조회")
    @Test
    void getReviewComments() {
        // Given
        Long reviewId = 1L;

        // WHen
        List<ReviewCommentDTO> commentDTOList = reviewCommentService.findAllComment(reviewId);
        for (ReviewCommentDTO commentDTO : commentDTOList) {
            log.info(commentDTO.toString());
        }

        // Then
        assertNotNull(commentDTOList);
        assertTrue(commentDTOList.size() > 0);
        assertTrue(commentDTOList.get(0).getCommentId().equals(reviewId));
    }

    @DisplayName("리뷰에 새로운 댓글 생성")
    @Test
    void createReviewComment() {
        // Given
        Long reviewId = 1L;
        Long userId = 1L;
        String content = "test";

        ReviewCommentDTO reviewCommentDTO = new ReviewCommentDTO();
        reviewCommentDTO.setReviewId(reviewId);
        reviewCommentDTO.setContent(content);

        // When
        reviewCommentService.createReviewComment(userId, reviewCommentDTO);
        ReviewCommentDTO newComment = reviewCommentService.findAllComment(reviewId).get(10);

        // THen
        assertNotNull(newComment);
        assertEquals(content, newComment.getContent());
        assertTrue(newComment.getReviewId().equals(reviewId));
    }

    @DisplayName("리뷰에 댓글 수정")
    @Test
    void updateReviewComment() {
        Long reviewId = 1L;
        Long commentId = 1L;
        Long userId = 6L;
        String content = "test";

        ReviewCommentDTO reviewCommentDTO = new ReviewCommentDTO();
        reviewCommentDTO.setCommentId(commentId);
        reviewCommentDTO.setContent(content);

        reviewCommentService.updateReviewComment(userId, reviewCommentDTO);
        ReviewCommentDTO newComment = reviewCommentService.findAllComment(reviewId).get(0);

        assertNotNull(newComment);
        assertEquals(content, newComment.getContent());
        assertTrue(newComment.getReviewId().equals(reviewId));
    }

    @DisplayName("리뷰에 댓글 삭제")
    @Test
    void deleteReviewComment() {
        Long reviewId = 1L;
        Long commentId = 1L;
        Long userId = 6L;

        ReviewCommentDTO dto  = new ReviewCommentDTO();
        dto.setCommentId(commentId);

        reviewCommentService.deleteReviewComment(userId, dto);
        ReviewCommentDTO deletedComment = reviewCommentService.findAllComment(reviewId).get(0);

        assertNotNull(deletedComment);
        assertEquals(commentId, deletedComment.getCommentId());
        assertEquals(deletedComment.getContent(),"삭제된 댓글입니다.");

    }
}