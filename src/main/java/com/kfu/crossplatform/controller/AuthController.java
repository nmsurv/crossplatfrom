package com.kfu.crossplatform.controller;

import com.kfu.crossplatform.dto.LoginRequest;
import com.kfu.crossplatform.service.AuthenticationService;
import com.kfu.crossplatform.dto.LoginResponse;
import com.kfu.crossplatform.dto.UserLogged;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
        @CookieValue(name="access-token", required = false) String access,
        @CookieValue(name="refresh-token", required = false) String refresh,
        @RequestBody LoginRequest loginRequest) {
            return authenticationService.login(loginRequest, access, refresh);
        }
    
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
        @CookieValue(name = "refresh-token", required = false) String refresh) {
            return authenticationService.refresh(refresh);
        }
    
    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(
        @CookieValue(name = "access-token", required = false) String access) {
            return authenticationService.logout(access);
        }
    
    @GetMapping("/info")
    public ResponseEntity<UserLogged> info(){
        return ResponseEntity.ok(authenticationService.info());
    }

}
