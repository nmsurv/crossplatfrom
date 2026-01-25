package com.kfu.crossplatform.service;

import java.util.Set;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.kfu.crossplatform.dto.LoginRequest;
import com.kfu.crossplatform.dto.LoginResponse;
import com.kfu.crossplatform.dto.UserLogged;
import com.kfu.crossplatform.repository.TokenRepository;
import com.kfu.crossplatform.repository.UserRepository;
import com.kfu.crossplatform.utils.CookieUtil;
import com.kfu.crossplatform.jwt.JwtTokenProvider;
import com.kfu.crossplatform.mapper.UserMapper;
import com.kfu.crossplatform.domain.Token;
import com.kfu.crossplatform.domain.User;



import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    public final UserService userService;
    public final TokenRepository tokenRepository;
    public final UserRepository userRepository;
    public final CookieUtil cookieUtil;
    public final AuthenticationManager authenticationManager;
    public final JwtTokenProvider jwtTokenProvider;
    public final TelegramService telegramService;

    @Value("${jwt.access.duration.seconds}")
    private long accessTokenDurationSecond;
    @Value("${jwt.access.duration.minutes}")
    private long accessTokenDurationMinute;

    @Value("${jwt.refresh.duration.seconds}")
    private long refreshTokenDurationSecond;
    @Value("${jwt.refresh.duration.days}")
    private long refreshTokenDurationDay;

    private void addAccessTokenCookie(HttpHeaders headers, Token token){
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessCookie(token.getValue(), accessTokenDurationSecond).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders headers, Token token){
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshCookie(token.getValue(), refreshTokenDurationSecond).toString());
    }

    private void revokeAllTokens(User user) {
        log.debug("Revoking tokens for user={}", user.getUsername());
        Set<Token> tokens = user.getTokens();
        tokens.forEach(token -> {
            if(token.getExpiryDate().isBefore(LocalDateTime.now())) {
                tokenRepository.delete(token);
                log.debug("Expired token deleted for user={}", user.getUsername());
            } else if (!token.isDisabled()) {
                token.setDisabled(true);
                tokenRepository.save(token);
                log.debug("Active token disabled for user={}", user.getUsername());
            }
        });
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request, String access, String refresh) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = userService.getUser(request.username());
        boolean accessValid = jwtTokenProvider.isValid(access);
        boolean refreshValid = jwtTokenProvider.isValid(refresh);

        log.debug( "Token validity for user={}: accessValid={}, refreshValid={}",
            user.getUsername(), accessValid, refreshValid);

        HttpHeaders headers = new HttpHeaders();
        revokeAllTokens(user);

         if (!accessValid) {
            Token newAccess = jwtTokenProvider.generateAccessToken(Map.of("role", user.getRole().getAuthority()),
                    accessTokenDurationMinute, ChronoUnit.MINUTES, user);
            newAccess.setUser(user);
            addAccessTokenCookie(headers, newAccess);
            tokenRepository.save(newAccess);

            log.info("New access token issued for user={}", user.getUsername());
        }
        
        if(!refreshValid) {
            Token newRefresh = jwtTokenProvider.generateRefreshToken(refreshTokenDurationDay, ChronoUnit.DAYS, user);
            newRefresh.setUser(user);
            addRefreshTokenCookie(headers, newRefresh);
            tokenRepository.save(newRefresh);

            log.info("New refresh token issued for user={}", user.getUsername());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("User '{}' successfully authenticated with role={}",
            user.getUsername(),user.getRole().getTitle());
        telegramService.sendMessage(
            "Пользователь " + user.getUsername() + " вошел в систему!"
        );

        LoginResponse loginResponse = new LoginResponse(true, user.getRole().getTitle());
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    public ResponseEntity<LoginResponse> refresh(String refreshToken) {
        log.info("Refreshing access token");

        if(!jwtTokenProvider.isValid(refreshToken)) {
            log.warn("Invalid refresh token used");
            throw new RuntimeException("token is invalid");
        }
        User user = userService.getUser(jwtTokenProvider.getUsername(refreshToken));
        HttpHeaders headers = new HttpHeaders();
        Token newAccess = jwtTokenProvider.generateAccessToken(Map.of("role", user.getRole().getAuthority()),
                accessTokenDurationMinute, ChronoUnit.MINUTES, user);
        newAccess.setUser(user);
        tokenRepository.save(newAccess);
        addAccessTokenCookie(headers, newAccess);

        log.info("Access token refreshed for user={}", user.getUsername());

        LoginResponse loginResponse = new LoginResponse(true, user.getRole().getTitle());
        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }
    public ResponseEntity<LoginResponse> logout(String access) {

        String username = jwtTokenProvider.getUsername(access);
        log.info("Logout initiated for user={}", username);

        SecurityContextHolder.clearContext();
        User user = userService.getUser(jwtTokenProvider.getUsername(access));
        revokeAllTokens(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessCookie().toString());
        headers.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshCookie().toString());

        log.info("User '{}' logout successfully", username);
        telegramService.sendMessage(
            "Пользователь " + user.getUsername() + " вышел из системы!"
        );
        return ResponseEntity.ok().headers(headers).body(new LoginResponse(false,null));
    }

    public UserLogged info() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth instanceof AnonymousAuthenticationToken) {
            log.warn("Attempt to get user info without authentication");
            throw new RuntimeException("No user");
        }
        User user = userService.getUser(auth.getName());

        log.debug("Fetched info for authenticated user={}", user.getUsername());

        return UserMapper.userToLoggedDTO(user);
    }
    
}

