package com.threeping.mudium.user.service;

import com.threeping.mudium.user.aggregate.dto.UserDTO;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import com.threeping.mudium.user.aggregate.vo.RequestRegistUserVO;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    UserDTO registUser(RequestRegistUserVO newUser);

    UserEntity findByUserIdentifier(String userIdentifier);

    UserDTO findByUserId(Long userId);

    boolean checkUniqueNickname(String nickname);

    boolean checkIfEmailAlreadyUsed(String email);
}
