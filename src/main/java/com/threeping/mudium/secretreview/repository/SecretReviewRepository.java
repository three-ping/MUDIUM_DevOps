package com.threeping.mudium.secretreview.repository;

import com.threeping.mudium.secretreview.aggregate.entity.ActiveStatus;
import com.threeping.mudium.secretreview.aggregate.entity.SecretReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecretReviewRepository extends JpaRepository<SecretReview, Long> {
    // 비밀리뷰 전체 조회
    List<SecretReview> findAllByUser_UserIdAndActiveStatus(Long userId, ActiveStatus activeStatus);

    // 비밀리뷰 상세 조회
    List<SecretReview> findByUser_UserIdAndSecretReviewIdAndActiveStatus(
            Long userId, Long secretReviewId, ActiveStatus activeStatus);
}
