package com.threeping.mudium.user.aggregate.vo;

import lombok.Data;

@Data
public class OAuth2LoginVO {
    private String userId;
    private String userName;
    private String nickname;
    private String email;
    private String pwd;
    private String accessToken;
    private String refreshToken;
    private String profileImage;
    private String signupPath;
}
