package com.kfu.crossplatform.jwt;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.kfu.crossplatform.enums.TokenType;
import com.kfu.crossplatform.domain.Token;
import com.kfu.crossplatform.repository.TokenRepository;

@RequiredArgsConstructor
@Service
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    
    @Value("${jwt.secret}")
    private String key;

    private final TokenRepository tokenRepository;

    private boolean isDisabled(String value) {
        Token token = tokenRepository.findByValue(value).orElse(null);
        if(token == null) {
            logger.debug("Token not found in database");
            return true;
        }
        boolean disabled = token.isDisabled();
        logger.debug("Token found in DB, disabled={}", disabled);
        return disabled;
    }
    private Date toDate(LocalDateTime time) {
            return Date.from(time.toInstant(ZoneOffset.UTC));
    }

    private LocalDateTime toLocalDateTime(Date time){
        return time.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    private Claims extractAllClaims(String value) {
        return Jwts.parserBuilder().setSigningKey(decodeSecretKey(key)).build().parseClaimsJws(value).getBody();
    }

    private Key decodeSecretKey(String key) {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(key));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }
    
    public String getUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public LocalDateTime getExpiration(String token) {
        return toLocalDateTime(extractClaim(token, Claims::getExpiration));
    }

    public boolean isValid(String token){
        if(token == null) {
            logger.debug("Token is null");
            return false;
        }
        try{
            Jwts.parserBuilder().setSigningKey(decodeSecretKey(key)).build().parseClaimsJws(token);
            logger.debug("JWT signature valid, checking if disabled");
            boolean valid = !isDisabled(token);
            logger.debug("Token valid={}", valid);
            return valid;
        }
        catch(JwtException e){
            logger.debug("JWT parsing failed: {}", e.getMessage());
            return false;
        }
    }

    public Token generateAccessToken(Map<String, Object> extra, long duration, TemporalUnit durationType, UserDetails user){
        String username = user.getUsername();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expirationDate = now.plus(duration, durationType);

        String value = Jwts.builder().setClaims(extra).setSubject(username).setIssuedAt(toDate(now))
            .setExpiration(toDate(expirationDate))
            .signWith(decodeSecretKey(key), SignatureAlgorithm.HS256).compact();

        return new Token(TokenType.ACCESS, value, expirationDate, false, null);
    }

    public Token generateRefreshToken(long duration, TemporalUnit durationType, UserDetails user) {
        String username = user.getUsername();

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expirationDate = now.plus(duration, durationType);

        String value = Jwts.builder().setSubject(username).setIssuedAt(toDate(now))
                .setExpiration(toDate(expirationDate))
                .signWith(decodeSecretKey(key), SignatureAlgorithm.HS256).compact();

        return new Token(TokenType.REFRESH, value, expirationDate, false, null);
    }
}
