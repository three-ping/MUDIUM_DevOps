package com.threeping.mudium.reviewlike.aggregate.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReviewLikePK implements Serializable {
    private Long reviewId;
    private Long userId;
}
