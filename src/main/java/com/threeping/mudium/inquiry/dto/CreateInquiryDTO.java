package com.threeping.mudium.inquiry.dto;

import com.threeping.mudium.user.aggregate.entity.UserEntity;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CreateInquiryDTO {
    private String title;
    private String content;
    private Timestamp createdAt;
    private UserEntity user;
}
