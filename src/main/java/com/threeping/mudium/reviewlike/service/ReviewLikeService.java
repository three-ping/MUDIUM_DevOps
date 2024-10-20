package com.threeping.mudium.reviewlike.service;

public interface ReviewLikeService {
    void createReviewLike(Long reviewId, Long userId);

    void deleteReviewLike(Long reviewId, Long userId);

    Boolean findReviewLike(Long reviewId, Long userId);
}
