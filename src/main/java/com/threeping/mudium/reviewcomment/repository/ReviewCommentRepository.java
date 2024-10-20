package com.threeping.mudium.reviewcomment.repository;

import com.threeping.mudium.reviewcomment.aggregate.ActiveStatus;
import com.threeping.mudium.reviewcomment.aggregate.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    @Query("SELECT rc, (SELECT u.nickname FROM UserEntity u WHERE u.userId = rc.userId) as nickName " +
            "FROM reviewCommentEntity rc WHERE rc.reviewId = :reviewId")
    List<Object[]> findAllReviewCommentByReviewId(Long reviewId);

    Optional<ReviewComment> findByCommentIdAndUserIdAndActiveStatus(Long commentId, Long userId, ActiveStatus activeStatus);
}
