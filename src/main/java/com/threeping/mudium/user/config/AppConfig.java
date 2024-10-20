package com.threeping.mudium.user.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class AppConfig {

    /* Security 자체에서 사용할 암호화 방식 용 Bean 추가 */
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* RestTemplate Bean 추가 */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {

            @Override
            public void apply(RequestTemplate requestTemplate) {

                /* 설명. 현재 요청의 Http Servelet Request를 가져옴 */
                ServletRequestAttributes requestAttributes =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if(requestAttributes != null) {

                    /* 설명. 현재 요청의 Authorization 헤더 추출 (Bearer 토큰) */
                    String authorizationHeader = requestAttributes
                            .getRequest()           // request 객체 추출
                            .getHeader(HttpHeaders.AUTHORIZATION);

                    if(authorizationHeader != null) {       // 토큰을 들고 왔다면

                        /* 설명. Feign client 요청에 "Authorization" 헤더 추가 */
                        requestTemplate.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
                    }
                }
            }
        };
    }
}
