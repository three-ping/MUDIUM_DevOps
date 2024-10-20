package com.threeping.mudium.review.repository;

import com.threeping.mudium.review.aggregate.entity.ActiveStatus;
import com.threeping.mudium.review.aggregate.entity.Review;
import com.threeping.mudium.review.dto.ReviewWithScopeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 리뷰 전체 조회
    List<Review> findAllByMusical_MusicalIdAndActiveStatus(Long musicalId, ActiveStatus activeStatus);

    // 리뷰 상세 조회
    List<Review> findByMusical_MusicalIdAndReviewIdAndActiveStatus(Long musicalId, Long reviewId, ActiveStatus activeStatus);

    @Query("SELECT new com.threeping.mudium.review.dto.ReviewWithScopeDTO(r.reviewId, r.content, sc.scope, r.user.userId, r.user.nickname, r.musical.musicalId) " +
            "FROM Review r " +
            "LEFT OUTER JOIN ScopeEntity sc ON r.user.userId = sc.userId AND r.musical.musicalId = sc.musicalId " +
            "WHERE r.musical.musicalId = :musicalId AND r.activeStatus = 'ACTIVE'")
    List<ReviewWithScopeDTO> findReviewsWithRatingsByMusicalId(@Param("musicalId") Long musicalId);

    Optional<Review> findReviewByReviewIdAndActiveStatus(Long reviewId, ActiveStatus activeStatus);

    // 리뷰 userId로 조회
    List<Review> findAllByUser_UserIdAndActiveStatus(Long userId, ActiveStatus activeStatus);

}
