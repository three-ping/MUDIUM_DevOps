package com.threeping.mudium.user.service;

import com.threeping.mudium.user.aggregate.dto.UserDTO;
import com.threeping.mudium.user.aggregate.vo.OAuth2LoginVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface OAuth2UserService extends UserDetailsService {

    OAuth2LoginVO processKakaoUser(String code);
}
