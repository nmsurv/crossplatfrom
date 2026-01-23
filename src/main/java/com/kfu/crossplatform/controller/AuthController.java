package com.kfu.crossplatform.controller;

import com.kfu.crossplatform.dto.LoginRequest;
import com.kfu.crossplatform.dto.LoginResponse;
import com.kfu.crossplatform.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }
}
