package com.threeping.mudium.secretreview.dto;

import com.threeping.mudium.secretreview.aggregate.entity.ActiveStatus;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SecretReviewResponseDTO {
    private Long secretReviewId;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private ActiveStatus activeStatus;
    private Long musicalId;
    private Long userId;
    private String userNickname;
    private String userProfile;
    private String musicalTitle;
}
