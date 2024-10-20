package com.threeping.mudium.user.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.user.aggregate.dto.UserDTO;
import com.threeping.mudium.user.aggregate.entity.SignupPath;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import com.threeping.mudium.user.aggregate.vo.OAuth2LoginVO;
import com.threeping.mudium.user.aggregate.vo.RequestRegistUserVO;
import com.threeping.mudium.user.repository.UserRepository;
import com.threeping.mudium.user.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
public class OAuth2UserServiceImpl implements OAuth2UserService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public OAuth2UserServiceImpl(UserRepository userRepository
            , UserService userService
            , JwtUtil jwtUtil
    , RestTemplate restTemplate, ModelMapper modelMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userIdentifier) throws UsernameNotFoundException {

        UserEntity loginUser = userRepository.findByUserIdentifier(userIdentifier);
        if (loginUser == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER_ID);
        }

        String encryptedPwd = loginUser.getEncryptedPwd();
        if (encryptedPwd == null) {
            encryptedPwd = "{noop}";
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        return new User(loginUser.getUserIdentifier()
                , encryptedPwd
                , true
                , true
                , true
                , true
                , grantedAuthorities);
    }

    @Override
    public OAuth2LoginVO processKakaoUser(String code) {
        log.info("OAuth2UserService - code: {}", code);

        String accessToken = getKakaoAccessToken(code);
        Map<String, Object> userInfo = getKakaoUserInfo(accessToken);

        String kakaoId = String.valueOf(userInfo.get("id"));
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("nickname");
        log.info("kakaoId: {}", kakaoId);
        log.info("email: {}", email);
        log.info("name: {}", name);
        if (email == null || email.isEmpty()) {
            email = kakaoId + "@kakao.com";
        }

        UserEntity userEntity = userRepository.findByUserIdentifier("KAKAO_" + email);

        if (userEntity == null) {
            log.info("regist new Kakao User: {}", name);

            RequestRegistUserVO newUser = new RequestRegistUserVO();
            newUser.setEmail(email);
            newUser.setUserName(name != null ? name : "KakaoUser");
//            newUser.setUserAuthId(kakaoId);
            newUser.setPassword(UUID.randomUUID().toString());
            newUser.setSignupPath(SignupPath.KAKAO);
            newUser.setNickname("Kakao@"+kakaoId);
            log.info("regist newUser: {}", newUser);
            userService.registUser(newUser);

            userEntity = userRepository.findByUserIdentifier("KAKAO_" + email);

        }
        OAuth2LoginVO user = modelMapper.map(userEntity, OAuth2LoginVO.class);

        user.setAccessToken(accessToken);

        log.info("userEntity: {}", userEntity);
        String refreshToken = jwtUtil.generateRefreshToken(userEntity, new ArrayList<>());
        log.info("refreshToken: {}", refreshToken);
        user.setRefreshToken(refreshToken);

        return user;
    }

    private String getKakaoAccessToken(String code) {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "454fe5e6a0e0c020cf155003e27761e2");  // Kakao Developer에서 얻은 클라이언트 ID
        params.add("client_secret", "3pzUaMGh7JwXxKU9c1HajsQnNUQ0jDAN");  // Kakao Developer에서 얻은 클라이언트 시크릿
        params.add("redirect_uri", "http://127.0.0.1:8080/api/users/oauth2/kakao");  // 등록한 리다이렉트 URI
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    kakaoAuthUrl,
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } catch (HttpClientErrorException e) {
            log.error("Kakao API error: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    private Map<String, Object> getKakaoUserInfo(String accessToken) {
        String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    kakaoUserInfoUrl,
                    HttpMethod.POST,
                    kakaoUserInfoRequest,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
            Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;

            Map<String, Object> userInfo = new HashMap<>();
            // userInfo.put("email", kakaoAccount != null ? kakaoAccount.get("email") : null);
            userInfo.put("id", responseBody.get("id"));
            userInfo.put("nickname", profile != null ? profile.get("nickname") : null);

            return userInfo;
        } catch (HttpClientErrorException e) {
            log.error("Kakao API error: {}", e.getResponseBodyAsString());
            throw e;
        }
    }
}

