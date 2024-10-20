package com.threeping.mudium.musicalboard.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MusicalPostListDTO {

    private Long postId;

    private String title;

    private String writer;

    private Long likeCount;

    private String viewCount;

    private String createdAt;

    private Long commentCount;
}
