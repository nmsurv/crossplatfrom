package com.kfu.crossplatform.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter 
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
