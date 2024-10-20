package com.threeping.mudium.notice.dto;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateNoticeDTO {
    private Long userId;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
