package com.threeping.mudium.secretreview.service;

import com.threeping.mudium.secretreview.dto.SecretReviewRequestDTO;
import com.threeping.mudium.secretreview.dto.SecretReviewResponseDTO;

import java.util.List;

public interface SecretReviewService{
    // 비밀리뷰 전체 조회
    List<SecretReviewResponseDTO> findSecretReviewByUserId(Long userId);

    // 비밀리뷰 상세 조회
    List<SecretReviewResponseDTO> findSecretReviewByUserIdAndSecretReviewId(Long userId, Long secretReviewId);

    // 비밀리뷰 작성
    void createSecretReview(Long musicalId, SecretReviewRequestDTO secretReviewRequestDTO);

    // 비밀리뷰 수정
    void updateSecretReview(Long secretReviewId, SecretReviewRequestDTO secretReviewRequestDTO);

    // 비밀리뷰 삭제
    void deleteSecretReview(Long secretReviewId, Long userId);
}
