package com.threeping.mudium.reviewcomment.service;

import com.threeping.mudium.reviewcomment.dto.ReviewCommentDTO;

import java.util.List;

public interface ReviewCommentService {
    List<ReviewCommentDTO> findAllComment(Long reviewId);

    void createReviewComment(Long userId, ReviewCommentDTO dto);

    void updateReviewComment(Long userId, ReviewCommentDTO dto);

    void deleteReviewComment(Long userId, ReviewCommentDTO dto);
}
