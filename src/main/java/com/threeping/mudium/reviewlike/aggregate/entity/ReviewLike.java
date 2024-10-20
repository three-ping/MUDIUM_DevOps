package com.threeping.mudium.reviewlike.aggregate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "TBL_REVIEW_LIKE")
@IdClass(ReviewLikePK.class)
public class ReviewLike {
    @Id
    @Column(name="review_id")
    private Long reviewId;

    @Id
    @Column(name="user_id")
    private Long userId;
}
