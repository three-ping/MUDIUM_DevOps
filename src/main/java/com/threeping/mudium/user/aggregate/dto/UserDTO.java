package com.threeping.mudium.user.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.threeping.mudium.user.aggregate.entity.AcceptStatus;
import com.threeping.mudium.user.aggregate.entity.ActiveStatus;
import com.threeping.mudium.user.aggregate.entity.SignupPath;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserDTO {

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
    private String userIdentifier; // 가입 경로 + ID 조합

    // Builder 메서드 추가
    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    // Builder 클래스 추가
    public static class UserDTOBuilder {
        private Long userId;
        private String userName;
        private String password;
        private String nickname;
        private String email;
//        private String userAuthId; // 추가
        private ActiveStatus userStatus;
        private LocalDateTime createdAt;
        private LocalDateTime withdrawnAt;
        private String profileImage;
        private AcceptStatus acceptStatus;
        private SignupPath signupPath;
        private String userIdentifier;

        public UserDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDTOBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

//        public UserDTOBuilder userAuthId(String userAuthId) { // 추가
//            this.userAuthId = userAuthId;
//            return this;
//        }

        public UserDTOBuilder userStatus(ActiveStatus userStatus) {
            this.userStatus = userStatus;
            return this;
        }

        public UserDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserDTOBuilder withdrawnAt(LocalDateTime withdrawnAt) {
            this.withdrawnAt = withdrawnAt;
            return this;
        }

        public UserDTOBuilder profileImage(String profileImage) {
            this.profileImage = profileImage;
            return this;
        }

        public UserDTOBuilder acceptStatus(AcceptStatus acceptStatus) {
            this.acceptStatus = acceptStatus;
            return this;
        }

        public UserDTOBuilder signupPath(SignupPath signupPath) {
            this.signupPath = signupPath;
            return this;
        }

        public UserDTOBuilder userIdentifier(String userIdentifier) {
            this.userIdentifier = userIdentifier;
            return this;
        }

        public UserDTO build() {
            UserDTO userDTO = new UserDTO();
            userDTO.userId = this.userId;
            userDTO.userName = this.userName;
            userDTO.password = this.password;
            userDTO.nickname = this.nickname;
            userDTO.email = this.email;
//            userDTO.userAuthId = this.userAuthId; // 추가
            userDTO.userStatus = this.userStatus;
            userDTO.createdAt = this.createdAt;
            userDTO.withdrawnAt = this.withdrawnAt;
            userDTO.profileImage = this.profileImage;
            userDTO.acceptStatus = this.acceptStatus;
            userDTO.signupPath = this.signupPath;
            userDTO.userIdentifier = this.userIdentifier;
            return userDTO;
        }
    }
}
