package com.threeping.mudium.user.security;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import com.threeping.mudium.user.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private final Key secretKey;
    private final UserService userService;
    private final long accessExpirationTime;
    private final long refreshExpirationTime;

    @Autowired
    public JwtUtil(@Value("${token.secret}") String secretKey
            , UserService userService
            , @Value("${token.access-expiration-time}") long accessExpirationTime
            , @Value("${token.refresh-expiration-time}") long refreshExpirationTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.userService = userService;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    /* 설명. Token 검증(Bearer 토큰이 넘어왔고, 우리 사이트의 secret key로 만들어 졌는가, 만료되었는지와 내용이 비어있진 않은지) */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token: {}", e);
            throw new CommonException(ErrorCode.INVALID_TOKEN_ERROR);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token: {}", e);
            throw new CommonException(ErrorCode.EXPIRED_TOKEN_ERROR);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token: {}", e);
            throw new CommonException(ErrorCode.TOKEN_UNSUPPORTED_ERROR);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty: {}", e);
            throw new CommonException(ErrorCode.TOKEN_MALFORMED_ERROR);
        }
        return true;
    }

    /* 설명. 넘어온 AccessToken으로 인증객체 추출 */
    public Authentication getAuthentication(String token) {
        String userIdentifier = this.getUserId(token);

        /* 설명. 토큰을 들고 왔던 오지 않았던 로그인 시 동일하게 security가 관리할 UserDetails 타입 */
        UserDetails userDetails = userService.loadUserByUsername(userIdentifier);

        /* 설명. 토큰에서 claim 추출 (payload에 담긴 애들) */
        Claims claims = parseClaims(token);
        log.info("token: {}", token);
        log.info("claims: {}", claims);


        Collection<? extends GrantedAuthority> authorities;
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한정보가 없는 토큰입니다.");
        } else {
            /* 설명. Claim에서 권한 정보 가져오기
                인증되면 뒤 필터 동작 x
                인증 안되면 다음 필터 실행
            */

            authorities =
                    Arrays.stream(claims.get("auth").toString()
                                    .replace("[", "")
                                    .replace("]", "")
                                    .split(", "))
                            .map(role -> new SimpleGrantedAuthority(role))
                            .collect(Collectors.toList());
        }

        // principal: 관리될 객체를 적어야됨
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

    }

    /* 설명. Token에서 Claims 추출 */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /* 설명. Token에서 사용자의 id(subject claim) 추출 */
    public String getUserId(String token) {
        return parseClaims(token).getSubject();     // 이메일꺼냄
    }

    /* access token generate method */
    public String generateAccessToken(UserEntity user, List<String> roles){
        return Jwts.builder()
                .setSubject(user.getUserIdentifier())
                .claim("email", user.getEmail())
                .claim("auth", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /* refresh token generate method */
    public String generateRefreshToken(UserEntity user, List<String> roles){
        log.info("generate RefreshToken - user: {}", user);
        return Jwts.builder()
                .setSubject(user.getUserIdentifier())
                .claim("email",user.getEmail())
                .claim("auth",roles)
                .claim("userIdentifier",user.getUserIdentifier())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
