package com.jihunsns_back.api.controller;

import com.jihunsns_back.api.dto.request.user.CheckNicknameReq;
import com.jihunsns_back.api.dto.response.user.CheckNicknameRes;
import com.jihunsns_back.api.dto.response.user.UserSummaryRes;
import com.jihunsns_back.api.service.UserService;
import com.jihunsns_back.common.exception.BusinessException;
import com.jihunsns_back.common.exception.ErrorCode;
import com.jihunsns_back.common.response.ApiResponse;
import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/check-nickname")
    public ResponseEntity<ApiResponse <CheckNicknameRes>> checkNickname(@RequestBody CheckNicknameReq req) {
        boolean exists = userRepository.existsByNickname(req.getNickname());
        return ResponseEntity.ok(ApiResponse.ok(new CheckNicknameRes(exists)));

    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserSummaryRes>>me(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)){
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않습니다.");
        }
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok(ApiResponse.ok(UserSummaryRes.from(user)));
    }

}
