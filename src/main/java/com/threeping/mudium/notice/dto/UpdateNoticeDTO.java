package com.threeping.mudium.notice.dto;

import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateNoticeDTO {
    private Long noticeId;
    private Long userId;
    private String title;
    private String content;
    private Timestamp updatedAt;
}
