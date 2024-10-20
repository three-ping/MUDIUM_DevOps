package com.threeping.mudium.board.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardDetailDTO {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private Long userId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long boardLike;
    private Long viewCount;
    private Long comments;
}
