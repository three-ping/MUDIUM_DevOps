package com.threeping.mudium.reviewlike.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.reviewlike.service.ReviewLikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/review-like")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @Autowired
    private ReviewLikeController(ReviewLikeService reviewLikeService){
        this.reviewLikeService = reviewLikeService;
    }

    @GetMapping("{reviewId}/{userId}")
    private ResponseDTO<?> findReviewLike(@PathVariable Long reviewId,
                                          @PathVariable Long userId){
        Boolean isExist = reviewLikeService.findReviewLike(reviewId,userId);
        return ResponseDTO.ok(isExist);
    }

    @PostMapping("{reviewId}")
    private ResponseDTO<?> createReviewLike(@PathVariable Long reviewId,
                                            @RequestBody Long userId){
        reviewLikeService.createReviewLike(reviewId,userId);
        return ResponseDTO.ok(null);
    }

    @DeleteMapping("{reviewId}")
    private ResponseDTO<?> deleteReviewLike(@PathVariable Long reviewId,
                                            @RequestBody Long userId){
        reviewLikeService.deleteReviewLike(reviewId,userId);
        log.info("delete?");
        return ResponseDTO.ok(null);
    }

}
