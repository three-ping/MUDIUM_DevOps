package com.threeping.mudium.review.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.review.dto.ReviewRequestDTO;
import com.threeping.mudium.review.dto.ReviewResponseDTO;
import com.threeping.mudium.review.dto.ReviewWithScopeDTO;
import com.threeping.mudium.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 전체 조회
    @GetMapping("/{musicalId}")
    private ResponseDTO<?> findReviewByMusicalId(@PathVariable Long musicalId) {
        List<ReviewWithScopeDTO> dtoList = reviewService.findReviewsWithRatingsByMusicalId(musicalId);

        return ResponseDTO.ok(dtoList);
    }

    // 리뷰 상세 조회
    @GetMapping("/{musicalId}/{reviewId}")
    private ResponseDTO<?> findReviewByMusicalIdAndReviewId(@PathVariable Long musicalId,
                                                            @PathVariable Long reviewId) {
        List<ReviewResponseDTO> reviewResponseDTO = reviewService.findReviewByMusicalIdAndReviewId(musicalId, reviewId);

        return ResponseDTO.ok(reviewResponseDTO);
    }

    // userId로 리뷰 조회
    @GetMapping("")
    private ResponseDTO<?> findReviewByUserId(@RequestParam Long userId) {

        List<ReviewWithScopeDTO> reviewResponseDTO = reviewService.findReviewByUserId(userId);

        return ResponseDTO.ok(reviewResponseDTO);
    }

    // 리뷰 작성
    @PostMapping("/{musicalId}")
    private ResponseDTO<?> createReview(@PathVariable Long musicalId,
                                        @RequestBody ReviewRequestDTO reviewRequestDTO) {
        reviewService.createReview(musicalId, reviewRequestDTO);

        return ResponseDTO.ok("리뷰 등록 성공!!!");
    }

    // 리뷰 수정
    @PutMapping("{musicalId}/{reviewId}")
    private ResponseDTO<?> updateReview(@PathVariable Long musicalId,
                                        @PathVariable Long reviewId,
                                        @RequestBody ReviewRequestDTO reviewRequestDTO) {
        reviewService.updateReview(musicalId, reviewId, reviewRequestDTO);

        return ResponseDTO.ok("리뷰 수정 성공!!!");
    }

    // 리뷰 삭제
    @DeleteMapping("/{musicalId}/{reviewId}")
    private ResponseDTO<?> deleteReview(@PathVariable Long musicalId,
                                        @PathVariable Long reviewId,
                                        @RequestParam Long userId) {
        reviewService.deleteReview(musicalId, reviewId, userId);

        return ResponseDTO.ok("리뷰 삭제 성공!!!");
    }

    /* userId로 리뷰 조회 */
    @GetMapping("/users/{userId}")
    public ResponseDTO<?> findReviewAndScopesByUserId(@PathVariable Long userId) {
        return ResponseDTO.ok(reviewService.findReviewAndScopeByUserId(userId));
    }
}
