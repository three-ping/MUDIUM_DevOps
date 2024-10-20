package com.threeping.mudium.notice.dto;

import com.threeping.mudium.user.aggregate.entity.UserRole;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NoticeListDTO {
    private Long id;
    private UserRole userRole;
    private String title;
    private Long userId;
    private Timestamp createdAt;
}