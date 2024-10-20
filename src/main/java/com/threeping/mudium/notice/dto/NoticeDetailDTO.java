package com.threeping.mudium.notice.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NoticeDetailDTO {
    private Long noticeId;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
