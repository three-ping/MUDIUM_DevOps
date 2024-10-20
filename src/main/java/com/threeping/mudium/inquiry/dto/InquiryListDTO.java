package com.threeping.mudium.inquiry.dto;

import lombok.*;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class InquiryListDTO {
    private Long inquiryId;
    private String title;
    private Long userId;
    private Timestamp createdAt;
    private Long comments;
}