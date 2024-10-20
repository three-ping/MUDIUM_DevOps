package com.threeping.mudium.reviewcomment.service;


import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.mucialcomment.aggregate.MusicalComment;
import com.threeping.mudium.review.aggregate.entity.Review;
import com.threeping.mudium.review.service.ReviewService;
import com.threeping.mudium.reviewcomment.aggregate.ActiveStatus;
import com.threeping.mudium.reviewcomment.aggregate.ReviewComment;
import com.threeping.mudium.reviewcomment.dto.ReviewCommentDTO;
import com.threeping.mudium.reviewcomment.repository.ReviewCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewCommentServiceImpl implements ReviewCommentService {

    private ReviewCommentRepository reviewCommentRepository;
    private ReviewService reviewService;

    @Autowired
    public ReviewCommentServiceImpl(ReviewCommentRepository reviewCommentRepository, ReviewService reviewService) {
        this.reviewCommentRepository = reviewCommentRepository;
        this.reviewService = reviewService;
    }

    @Override
    public List<ReviewCommentDTO> findAllComment(Long reviewId) {
        List<Object[]> results = reviewCommentRepository.findAllReviewCommentByReviewId(reviewId);

        List<ReviewCommentDTO> dtoList = results.stream()
                .map(result -> {
                    ReviewComment comment = (ReviewComment) result[0];
                    String nickName = (String) result[1];

                    ReviewCommentDTO dto = new ReviewCommentDTO();
                    dto.setCommentId(comment.getCommentId());
                    dto.setReviewId(comment.getReviewId());
                    if(comment.getActiveStatus().equals(ActiveStatus.INACTIVE)) {
                        dto.setContent("삭제된 댓글입니다.");
                        return dto;
                    }
                    dto.setContent(comment.getContent());
                    dto.setCreatedAt(timeConverter(comment.getCreatedAt()));
                    dto.setUpdatedAt(timeConverter(comment.getUpdatedAt()));
                    dto.setNickName(nickName);
                    return dto;
                }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public void createReviewComment(Long userId, ReviewCommentDTO dto) {
        if(!reviewService.existingCheck(dto.getReviewId()))
            throw new CommonException(ErrorCode.NOT_FOUND_REVIEW);

        if(dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new CommonException(ErrorCode.MISSING_REQUIRED_CONTENT);
        }

        ReviewComment comment = new ReviewComment();
        comment.setCreatedAt(Timestamp.from(Instant.now()));
        comment.setReviewId(dto.getReviewId());
        comment.setContent(dto.getContent());
        comment.setUserId(userId);
        comment.setActiveStatus(ActiveStatus.ACTIVE);
        comment.setUpdatedAt(Timestamp.from(Instant.now()));

        reviewCommentRepository.save(comment);
    }

    @Override
    public void updateReviewComment(Long userId, ReviewCommentDTO dto) {
        if(dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new CommonException(ErrorCode.MISSING_REQUIRED_CONTENT);
        }

        ReviewComment beforeComment = reviewCommentRepository.findByCommentIdAndUserIdAndActiveStatus
                        (dto.getCommentId(), userId, ActiveStatus.ACTIVE)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_REVIEW_COMMENT));

        beforeComment.setContent(dto.getContent());
        beforeComment.setUpdatedAt(Timestamp.from(Instant.now()));

        reviewCommentRepository.save(beforeComment);
    }

    @Override
    public void deleteReviewComment(Long userId, ReviewCommentDTO dto) {
        Long commentId = dto.getCommentId();

        ReviewComment existingComment = reviewCommentRepository.findByCommentIdAndUserIdAndActiveStatus
                (commentId, userId, ActiveStatus.ACTIVE)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_REVIEW_COMMENT));
        existingComment.setActiveStatus(ActiveStatus.INACTIVE);

        reviewCommentRepository.save(existingComment);
    }

    private String timeConverter(Timestamp date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
