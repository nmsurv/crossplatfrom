package com.kfu.crossplatform.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kfu.crossplatform.domain.Token;
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String value);
}
