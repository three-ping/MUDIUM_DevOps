package com.threeping.mudium.reviewcomment.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.reviewcomment.dto.ReviewCommentDTO;
import com.threeping.mudium.reviewcomment.service.ReviewCommentService;
import com.threeping.mudium.reviewcomment.vo.ReviewCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/review-comment")
public class ReviewCommentController {

    private final ReviewCommentService reviewCommentService;

    @Autowired
    public ReviewCommentController(ReviewCommentService reviewCommentService) {
        this.reviewCommentService = reviewCommentService;
    }

    @GetMapping("/{reviewId}")
    public ResponseDTO<?> findReviewComment(@PathVariable Long reviewId) {
        List<ReviewCommentDTO> dtoList = reviewCommentService.findAllComment(reviewId);

        ResponseDTO<List<ReviewCommentDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dtoList);
        responseDTO.setSuccess(true);
        responseDTO.setHttpStatus(HttpStatus.OK);

        return responseDTO;
    }

    @PostMapping("/{reviewId}")
    public ResponseDTO<?> createReviewComment(@PathVariable Long reviewId, @RequestBody ReviewCommentVO commentVO) {
        ReviewCommentDTO dto = new ReviewCommentDTO();
        dto.setReviewId(reviewId);
        dto.setContent(commentVO.getContent());

        reviewCommentService.createReviewComment(commentVO.getUserId(), dto);

        return ResponseDTO.ok("생성 성공");
    }

    @PutMapping("/{commentId}")
    public ResponseDTO<?> updateReviewComment(@PathVariable Long commentId, @RequestBody ReviewCommentVO commentVO) {
        ReviewCommentDTO dto = new ReviewCommentDTO();
        dto.setCommentId(commentId);
        dto.setContent(commentVO.getContent());

        reviewCommentService.updateReviewComment(commentVO.getUserId(), dto);

        return ResponseDTO.ok("수정 성공");
    }

    @DeleteMapping("/{commentId}")
    public ResponseDTO<?> deleteReviewComment(@PathVariable Long commentId, @RequestBody ReviewCommentVO commentVO) {
        ReviewCommentDTO dto = new ReviewCommentDTO();
        dto.setCommentId(commentId);

        reviewCommentService.deleteReviewComment(commentVO.getUserId(), dto);

        return ResponseDTO.ok("삭제 성공");
    }
}
