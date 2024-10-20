package com.threeping.mudium.board.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateBoardDTO {
    private String title;
    private String content;
    private Long userId;
    private Long boardId;
    private Long viewCount;

}
