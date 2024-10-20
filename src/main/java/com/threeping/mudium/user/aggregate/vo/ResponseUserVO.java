package com.threeping.mudium.user.aggregate.vo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.threeping.mudium.user.aggregate.entity.AcceptStatus;
import com.threeping.mudium.user.aggregate.entity.ActiveStatus;
import com.threeping.mudium.user.aggregate.entity.SignupPath;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseUserVO {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("user_status")
    private ActiveStatus userStatus;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("withdrawn_at")
    private LocalDateTime withdrawnAt;

    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("accept_status")
    private AcceptStatus acceptStatus;

    @JsonProperty("signup_path")
    private SignupPath signupPath;

    @JsonProperty("user_identifier")
    private String userIdentifier; // 신규 추가
}
