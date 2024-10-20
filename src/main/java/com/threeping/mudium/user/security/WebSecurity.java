package com.threeping.mudium.user.security;

import com.threeping.mudium.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/* 설명. security on/off */
@Configuration
@EnableWebSecurity
public class
WebSecurity {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;
    private Environment env;
    private JwtUtil jwtUtil;

    @Autowired
    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder,UserService userService
            ,Environment env,JwtUtil jwtUtil) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService=userService;
        this.env=env;
        this.jwtUtil=jwtUtil;
    }


    /* 설명. 인가(Authoriazation)용 메소드(인증 필터 추가) */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        /* 설명. csrf 비활성화 */
        http.csrf((csrf) -> csrf.disable());

        /* 설명. 로그인 시 추가할 authenticationManager 만들기 */
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.authorizeHttpRequests((authz) ->
                        authz
                                .requestMatchers(new AntPathRequestMatcher("/api/users/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/api/users/health")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/api/users/**")).permitAll()
//                                .requestMatchers(new AntPathRequestMatcher("/api/users/**", "GET")).hasRole("MEMBER")
//                                .requestMatchers(new AntPathRequestMatcher("/api/users/**", "PATCH")).hasRole("MEMBER")
//                                .requestMatchers(new AntPathRequestMatcher("/api/users/**", "PUT")).hasRole("MEMBER")
//                                .requestMatchers(new AntPathRequestMatcher("/api/users/**", "DELETE")).hasRole("MEMBER")
                                .anyRequest().authenticated()
                )


                /* 설명. authenticationManager 등록(UserDetails를 상속받는 Service 계층 + BCrypt 암호화) */
                .authenticationManager(authenticationManager)


                /* 설명. session 방식을 사용하지 않음(JWT Token 방식 사용 시 설정할 내용) */
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(getAuthenticationFilter(authenticationManager))
                .addFilterBefore(new JwtFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class);



                return http.build();
    }

    /* 설명. 인증(Authentication)용 메소드(인증 필터 반환) */
    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, userService, env, bCryptPasswordEncoder);
        authenticationFilter.setFilterProcessesUrl("/api/users/login");
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return authenticationFilter;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


}