package com.threeping.mudium.user.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.user.aggregate.dto.UserDTO;
import com.threeping.mudium.user.aggregate.entity.*;
import com.threeping.mudium.user.aggregate.vo.RequestRegistUserVO;
import com.threeping.mudium.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository ,BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserEntity findByUserIdentifier(String userIdentifier) {
        return userRepository.findByUserIdentifier(userIdentifier);
    }

    @Override
    @Transactional
    public UserDTO registUser(RequestRegistUserVO newUser) {
        UserEntity existingUser = userRepository.findByUserIdentifier(newUser.getSignupPath() +"_"+ newUser.getEmail());
        /* 설명. ModelMapper는 경우에 따라 자의적인 판단으로 필드끼리 매핑하는 경우가 있어 정확히 일치되게 매칭하려면 strict 설정해야됨 */
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


        if(existingUser != null){
            throw new CommonException(ErrorCode.EXIST_USER_ID);
        }

        if(newUser.getNickname() == null){
            throw new CommonException(ErrorCode.INVALID_INPUT_VALUE);
        }
        UserEntity existingUserWithSameNickname = userRepository.findByNickname(newUser.getNickname());
        if(existingUserWithSameNickname!=null){
            throw new CommonException(ErrorCode.DUPLICATE_NICKNAME_EXISTS);
        }

        String defaultProfileImageUrl = "";

        UserDTO newUserDTO = UserDTO.builder()
//                .userAuthId(newUser.getUserAuthId())
                .userName(newUser.getUserName())
                .email(newUser.getEmail())
                .signupPath(newUser.getSignupPath())
                .createdAt(LocalDateTime.now().withNano(0))
                .acceptStatus(AcceptStatus.Y)
                .userStatus(ActiveStatus.ACTIVE)
                .nickname(newUser.getNickname())
                .profileImage(defaultProfileImageUrl)
//                .userIdentifier(newUser.getSignupPath()+ "_"+ newUser.getUserAuthId())
                .build();
        log.info("newUserDTO: {}", newUserDTO);
        UserEntity userEntity = modelMapper.map(newUserDTO, UserEntity.class);
        log.info("userEntity: {}", userEntity);
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(newUser.getPassword()));
        userEntity.setUserRole(UserRole.ROLE_MEMBER);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        log.info("savedUserEntity: {}", savedUserEntity);

        return modelMapper.map(savedUserEntity, UserDTO.class);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userIdentifier) throws UsernameNotFoundException{

        UserEntity loginUser = userRepository.findByUserIdentifier(userIdentifier);
        if(loginUser == null){
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
    public UserDTO findByUserId(Long userId) {
        UserEntity foundUser = userRepository.findByUserId(userId);
        if(foundUser == null || foundUser == null){
            throw new CommonException(ErrorCode.NOT_FOUND_USER_ID);
        }
        return modelMapper.map(foundUser, UserDTO.class);
    }

    @Override
    public boolean checkUniqueNickname(String nickname) {
        UserEntity user = userRepository.findByNickname(nickname);
        if(user == null){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkIfEmailAlreadyUsed(String email) {
        UserEntity foundUser =  userRepository.findByEmail(email);
        if(foundUser != null){
            throw new CommonException(ErrorCode.DUPLICATE_EMAIL_EXISTS);
        }
        return false;
    }
}
