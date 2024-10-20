package com.threeping.mudium.board.dto;

import com.threeping.mudium.user.aggregate.entity.UserEntity;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegistBoardDTO {
    private Long userId;
    private String title;
    private String content;
}
