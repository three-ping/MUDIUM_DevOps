package com.threeping.mudium.inquiry.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateInquiryDTO {
    private Long userId;
    private Long inquiryId;
    private String title;
    private String content;
    private Timestamp updatedAt;
}
