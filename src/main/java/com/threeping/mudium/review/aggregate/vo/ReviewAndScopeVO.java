package com.threeping.mudium.review.aggregate.vo;

import com.threeping.mudium.review.aggregate.entity.Review;
import com.threeping.mudium.scope.aggregate.entity.ScopeEntity;
import com.threeping.mudium.musical.aggregate.Musical;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAndScopeVO {
    // Musical information
    private Long musicalId;
    private String musicalTitle;
    private String musicalRating;
    private String reviewVideo;
    private String poster;
    private Long viewCount;
    private String production;
    private String synopsis;

    // User and Scope information
    private Long userId;
    private Double scope;
    private String userNickname;

    // Review information
    private Long reviewId;
    private String reviewContent;
    private Timestamp reviewCreatedAt;
    private Long reviewLikes;

    public static ReviewAndScopeVO from(ScopeEntity scope, Review review) {
        ReviewAndScopeVO vo = new ReviewAndScopeVO();

        if (scope != null) {
            vo.setUserId(scope.getUserId());
            vo.setScope(scope.getScope());
            vo.setUserNickname(scope.getUserNickname());
        }

        if (review != null) {
            Musical musical = review.getMusical();
            vo.setMusicalId(musical.getMusicalId());
            vo.setMusicalTitle(musical.getTitle());
            vo.setMusicalRating(musical.getRating());
            vo.setReviewVideo(musical.getReviewVideo());
            vo.setPoster(musical.getPoster());
            vo.setViewCount(musical.getViewCount());
            vo.setProduction(musical.getProduction());
            vo.setSynopsis(musical.getSynopsys());

            vo.setUserId(vo.getUserId() != null ? vo.getUserId() : review.getUser().getUserId());
            vo.setUserNickname(vo.getUserNickname() != null ? vo.getUserNickname() : review.getUser().getNickname());
            vo.setReviewId(review.getReviewId());
            vo.setReviewContent(review.getContent());
            vo.setReviewCreatedAt(review.getCreatedAt());
            vo.setReviewLikes(review.getLike());
        }

        return vo;
    }
}