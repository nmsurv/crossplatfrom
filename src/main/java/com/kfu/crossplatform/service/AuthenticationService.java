package com.kfu.crossplatform.service;

import com.kfu.crossplatform.domain.User;
import com.kfu.crossplatform.dto.LoginRequest;
import com.kfu.crossplatform.dto.LoginResponse;
import com.kfu.crossplatform.jwt.JwtTokenProvider;
import com.kfu.crossplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user);
        return new LoginResponse(token);
    }
}
