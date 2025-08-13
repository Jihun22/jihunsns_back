package com.jihunsns_back.api.service;


import com.jihunsns_back.api.dto.request.auth.LoginReq;
import com.jihunsns_back.api.dto.request.auth.SignupReq;
import com.jihunsns_back.api.dto.response.auth.TokenRes;
import com.jihunsns_back.common.exception.BusinessException;
import com.jihunsns_back.common.exception.ErrorCode;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.UserRepository;
import com.jihunsns_back.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtProvider  jwtProvider;
    private final StringHttpMessageConverter stringHttpMessageConverter;

    @Transactional
    public void signup(SignupReq req) {
       if (userRepository.existsByEmail(req.email())) {
           throw new BusinessException(ErrorCode.USERNAME_DUPLICATED,"이미 사용 중인 이메일 입니다.");
       }
       User user = new User();
       user.setEmail(req.email());
       user.setPassword(passwordEncoder.encode(req.password()));
       user.setNickname(req.nickname());
       userRepository.save(user);
   }

   @Transactional(readOnly = true)
    public TokenRes login(LoginReq req) {
       User user = userRepository.findByEmail(req.email())
               .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED,"이메일 또는 비밀번호가 올바르지 않습니다."));

       if (!passwordEncoder.matches(req.password(), user.getPassword())) {
           throw new BusinessException(ErrorCode.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다.");

       }
       String access = jwtProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
       String refresh = jwtProvider.generateRefreshToken(user.getId());

       Instant accessExp = Instant.now().plusSeconds(60L* 60L);
       return new TokenRes(access, refresh, accessExp);
   }
    @Transactional(readOnly = true)
    public TokenRes refresh(String refreshToken) {
        Long userId = Long.parseLong(jwtProvider.parse(refreshToken).getBody().getSubject());
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
        String newAccess = jwtProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRole());
        String newRefresh = jwtProvider.generateRefreshToken(user.getId()); // 전략에 따라 재발급 or 단발성
        Instant accessExp = Instant.now().plusSeconds(60L * 60L);
        return new TokenRes(newAccess, newRefresh, accessExp);
    }
}