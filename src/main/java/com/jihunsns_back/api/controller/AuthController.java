package com.jihunsns_back.api.controller;


import com.jihunsns_back.api.dto.request.auth.LoginReq;
import com.jihunsns_back.api.dto.request.auth.SignupReq;
import com.jihunsns_back.api.dto.response.auth.TokenRes;
import com.jihunsns_back.api.service.AuthService;
import com.jihunsns_back.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse <Void>> signup(@Valid @RequestBody SignupReq req){
        authService.signup(req);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenRes>>login(@Valid @RequestBody LoginReq req) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(req)));
    }

    @Operation(summary = " 리프레시 토큰으로 엑세스 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRes>> refresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(ApiResponse.ok(authService.refresh(refreshToken)));
    }
}
