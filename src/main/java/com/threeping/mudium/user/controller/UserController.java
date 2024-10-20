package com.threeping.mudium.user.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.user.aggregate.dto.UserDTO;
import com.threeping.mudium.user.aggregate.vo.RequestRegistUserVO;
import com.threeping.mudium.user.aggregate.vo.ResponseUserVO;
import com.threeping.mudium.user.service.EmailVerificationService;
import com.threeping.mudium.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final Environment env;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    public UserController(Environment env, ModelMapper modelMapper, UserService userService, EmailVerificationService emailVerificationService) {
        this.env = env;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/health")
    public String healthCheck(){
        return "I'm working in user service " + env.getProperty("local.server.port");
    }

    @PostMapping("/send-verification")
    public ResponseDTO<?> sendVerificationEmail(@RequestParam String email) {

        userService.checkIfEmailAlreadyUsed(email);
        emailVerificationService.sendVerificationCode(email);
        return ResponseDTO.ok("이메일 인증 코드가 전송되었습니다.");
    }

    @PostMapping("/verify-code")
    public ResponseDTO<?> verifyEmailCode(@RequestParam String email, @RequestParam String code) {
        boolean isVerified = emailVerificationService.verifyCode(email, code);
        if (isVerified) {
            return ResponseDTO.ok(true);
        } else {
            return ResponseDTO.fail(new CommonException(ErrorCode.INVALID_VERIFICATION_CODE));
        }
    }

    @GetMapping("/check-nickname/{nickname}")
    public ResponseDTO<?> checkNickname(@PathVariable String nickname) {
        boolean isUnique = userService.checkUniqueNickname(nickname);
        return ResponseDTO.ok(isUnique);
    }

    @PostMapping("/signup")
    public ResponseDTO<?> signupUser(@RequestBody RequestRegistUserVO newUser) {
        UserDTO savedUserDTO = userService.registUser(newUser);
        ResponseUserVO responseUser = modelMapper.map(savedUserDTO, ResponseUserVO.class);
        return ResponseDTO.ok(responseUser);
    }

    @GetMapping("/{userId}")
    public ResponseDTO<?> getUser(@PathVariable Long userId) {
        return ResponseDTO.ok(userService.findByUserId(userId));
    }
}