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
import com.jihunsns_back.security.jwt.JwtAuthFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "사용자 관련 API")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(
            summary = "닉네임 중복 검사",
            description = "입력한 닉네임이 이미 사용 중인지 확인합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "중복 검사 성공",
            content = @Content(schema = @Schema(implementation = CheckNicknameSwaggerResponse.class))
    )
    @PostMapping("/check-nickname")
    public ResponseEntity<ApiResponse<CheckNicknameRes>> checkNickname(@RequestBody CheckNicknameReq req) {
        boolean exists = userRepository.existsByNickname(req.getNickname());
        return ResponseEntity.ok(ApiResponse.ok(new CheckNicknameRes(exists)));
    }

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 요약 정보를 조회합니다. (인증 필요)"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserSummarySwaggerResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserSummaryRes>> me(
            @Parameter(hidden = true) Authentication authentication
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtAuthFilter.UserPrincipal userPrincipal)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않습니다.");
        }

        String email = userPrincipal.email();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));

        return ResponseEntity.ok(ApiResponse.ok(UserSummaryRes.from(user)));
    }

    // ✅ Swagger 문서용 "포함" 래퍼 (상속 X)
    // ApiResponse의 실제 필드명에 맞춰 success/message/data 등으로 맞추면 됨.
    @Schema(name = "ApiResponse_CheckNicknameRes")
    static class CheckNicknameSwaggerResponse {
        @Schema(example = "true")
        public boolean success;

        @Schema(example = "성공")
        public String message;

        @Schema(implementation = CheckNicknameRes.class)
        public CheckNicknameRes data;
    }

    @Schema(name = "ApiResponse_UserSummaryRes")
    static class UserSummarySwaggerResponse {
        @Schema(example = "true")
        public boolean success;

        @Schema(example = "성공")
        public String message;

        @Schema(implementation = UserSummaryRes.class)
        public UserSummaryRes data;
    }
}