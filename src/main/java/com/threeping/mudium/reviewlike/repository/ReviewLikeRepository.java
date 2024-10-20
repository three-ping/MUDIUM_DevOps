package com.threeping.mudium.reviewlike.repository;

import com.threeping.mudium.reviewlike.aggregate.entity.ReviewLike;
import com.threeping.mudium.reviewlike.aggregate.entity.ReviewLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, ReviewLikePK> {
}
