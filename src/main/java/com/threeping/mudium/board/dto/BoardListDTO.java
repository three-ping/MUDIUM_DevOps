package com.threeping.mudium.board.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardListDTO {
    private Long boardId;
    private String title;
    private String nickname;
    private Long userId;
    private Timestamp createdAt;
    private Long comments;
    private Long viewCount;
    private Long boardLike;
}
