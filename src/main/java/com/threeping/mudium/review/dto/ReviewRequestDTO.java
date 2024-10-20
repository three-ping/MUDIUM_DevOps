package com.threeping.mudium.review.dto;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReviewRequestDTO {
    // @PathVariable로 받는 부분은 DTO에 작성하지 않아도 된다.
//    private Long reviewId;
    private String content;
//    private Timestamp createdAt;
//    private Long musicalId;
    private Long userId;
}
