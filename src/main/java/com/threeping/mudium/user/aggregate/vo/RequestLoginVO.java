package com.threeping.mudium.user.aggregate.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.threeping.mudium.user.aggregate.entity.SignupPath;
import lombok.Data;

@Data
public class RequestLoginVO {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("signup_path")
    private SignupPath signupPath; // 신규 추가


}
