package com.kfu.crossplatform.mapper;

import com.kfu.crossplatform.dto.UserDTO;
import com.kfu.crossplatform.dto.UserLogged;
import com.kfu.crossplatform.domain.User;
import com.kfu.crossplatform.domain.Permission;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO userToUserDTO(User user){
        return new UserDTO(user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getRole().getAuthority(),
            user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
    }

    public static UserLogged userToLoggedDTO(User user) {
        return new UserLogged(user.getUsername(),
            user.getRole().getAuthority(),
            user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
    }
}
