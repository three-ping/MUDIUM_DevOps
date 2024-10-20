package com.threeping.mudium.reviewlike.service;

import com.threeping.mudium.reviewlike.aggregate.entity.ReviewLike;
import com.threeping.mudium.reviewlike.aggregate.entity.ReviewLikePK;
import com.threeping.mudium.reviewlike.repository.ReviewLikeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewLikeServiceTests {

    private final ReviewLikeService reviewLikeService;
    private final ReviewLikeRepository reviewLikeRepository;

    @Autowired
    ReviewLikeServiceTests(ReviewLikeService reviewLikeService, ReviewLikeRepository reviewLikeRepository) {
        this.reviewLikeService = reviewLikeService;
        this.reviewLikeRepository = reviewLikeRepository;
    }

    @Test
    @DisplayName("리뷰 좋아요를 찾는다.")
    void findReviewLikeTest() {
        Long reviewId = -1L;
        Long userId = -1L;

        reviewLikeService.createReviewLike(reviewId, userId);
        Boolean result = reviewLikeService.findReviewLike(reviewId, userId);
        assertTrue(result, "좋아요가 존재해야 한다.");

        Boolean nonExistentResult = reviewLikeService.findReviewLike(-2L, -2L);
        assertFalse(nonExistentResult, "존재하지 않는 좋아요는 false여야 한다.");
    }

    @Test
    @DisplayName("리뷰 좋아요를 생성한다.")
    void createReviewLikeTest() {
        Long reviewId = -1L;
        Long userId = -1L;

        // 리뷰 좋아요를 생성합니다.
        reviewLikeService.createReviewLike(reviewId, userId);

        ReviewLikePK reviewLikePK = new ReviewLikePK(reviewId, userId);
        ReviewLike reviewLike = reviewLikeRepository.findById(reviewLikePK).orElse(null);
        assertNotNull(reviewLike, "좋아요가 저장되어 있어야 한다.");
    }

    @Test
    @DisplayName("리뷰 좋아요를 삭제한다.")
    void deleteReviewLikeTest() {
        Long reviewId = -1L;
        Long userId = -1L;

        reviewLikeService.createReviewLike(reviewId, userId);
        reviewLikeService.deleteReviewLike(reviewId, userId);

        ReviewLikePK reviewLikePK = new ReviewLikePK(reviewId, userId);
        ReviewLike reviewLike = reviewLikeRepository.findById(reviewLikePK).orElse(null);
        assertNull(reviewLike, "좋아요가 삭제되어 있어야 한다.");
    }


}