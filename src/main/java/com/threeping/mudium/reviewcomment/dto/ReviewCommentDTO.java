package com.threeping.mudium.reviewcomment.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewCommentDTO {

    private Long commentId;

    private Long reviewId;

    private String content;

    private String nickName;

    private String createdAt;

    private String updatedAt;

}
