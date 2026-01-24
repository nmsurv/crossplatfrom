package com.kfu.crossplatform.dto;
import java.util.Set;
public record UserLogged(String username, String role, Set<String> permissions) {
    
}
