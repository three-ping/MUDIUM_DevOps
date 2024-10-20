package com.threeping.mudium.user.repository;

import com.threeping.mudium.user.aggregate.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserIdentifier(String userIdentifier);

    UserEntity findByNickname(String nickname);

    UserEntity findByUserId(Long userId);

    UserEntity findByEmail(String email);
}
