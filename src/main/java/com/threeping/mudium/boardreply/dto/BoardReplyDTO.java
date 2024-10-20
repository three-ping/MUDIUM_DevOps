package com.threeping.mudium.boardreply.dto;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardReplyDTO {
    private Long boardReplyId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long boardCommentId;
    private Long userId;
    private String nickname;
}
