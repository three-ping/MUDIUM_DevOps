package com.threeping.mudium.secretreview.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SecretReviewRequestDTO {
    private String content;
    private Long userId;
}
