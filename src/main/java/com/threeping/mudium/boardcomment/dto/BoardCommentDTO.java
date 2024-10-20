package com.threeping.mudium.boardcomment.dto;

import com.threeping.mudium.board.aggregate.enumerate.ActiveStatus;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardCommentDTO {
    private Long boardCommentId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private ActiveStatus activeStatus;
    private Long boardId;
    private Long userId;
    private String nickname;
}
