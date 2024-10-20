package com.threeping.mudium.review.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewWithScopeDTO {

    private Long reviewId;

    private String content;

    private Double scope;

    private Long userId;

    private String nickName;

    private Long musicalId;

}
