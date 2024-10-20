package com.threeping.mudium.reviewlike.service;

import com.threeping.mudium.reviewlike.aggregate.entity.ReviewLike;
import com.threeping.mudium.reviewlike.aggregate.entity.ReviewLikePK;
import com.threeping.mudium.reviewlike.repository.ReviewLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;

    @Autowired
    ReviewLikeServiceImpl(ReviewLikeRepository reviewLikeRepository){
        this.reviewLikeRepository = reviewLikeRepository;
    }

    @Override
    @Transactional
    public Boolean findReviewLike(Long reviewId, Long userId) {
        ReviewLike reviewLike = reviewLikeRepository.findById(new ReviewLikePK(reviewId,userId)).orElse(null);
        return reviewLike != null;
    }

    @Override
    @Transactional
    public void createReviewLike(Long reviewId, Long userId) {
        ReviewLike reviewLike = new ReviewLike(reviewId,userId);
        reviewLikeRepository.save(reviewLike);
    }

    @Override
    @Transactional
    public void deleteReviewLike(Long reviewId, Long userId) {
        ReviewLikePK reviewLikePK = new ReviewLikePK(reviewId,userId);
        reviewLikeRepository.deleteById(reviewLikePK);
    }


}
