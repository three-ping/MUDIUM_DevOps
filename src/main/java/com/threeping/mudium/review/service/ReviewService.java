package com.threeping.mudium.review.service;

import com.threeping.mudium.review.aggregate.vo.ReviewAndScopeVO;
import com.threeping.mudium.review.dto.ReviewRequestDTO;
import com.threeping.mudium.review.dto.ReviewResponseDTO;
import com.threeping.mudium.review.dto.ReviewWithScopeDTO;

import java.util.List;

public interface ReviewService {
    // 리뷰 전체 조회
    List<ReviewResponseDTO> findReviewByMusicalId(Long musicalId);

    // 리뷰 상세 조회
    List<ReviewResponseDTO> findReviewByMusicalIdAndReviewId(Long musicalId, Long reviewId);

    // 리뷰 작성
    void createReview(Long musicalId, ReviewRequestDTO reviewRequestDTO);

    // 리뷰 수정
    void updateReview(Long musicalId, Long reviewId, ReviewRequestDTO reviewRequestDTO);

    // 리뷰 삭제
    void deleteReview(Long musicalId, Long reviewId, Long userId);

    List<ReviewWithScopeDTO> findReviewsWithRatingsByMusicalId(Long musicalId);

    boolean existingCheck(Long reviewId);

    // 리뷰 userId로 조회
    List<ReviewWithScopeDTO> findReviewByUserId(Long userId);

    /* find ReviewAndScopeVO with userId */
    List<ReviewAndScopeVO> findReviewAndScopeByUserId(Long userId);
}
