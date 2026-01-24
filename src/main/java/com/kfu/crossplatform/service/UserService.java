package com.kfu.crossplatform.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kfu.crossplatform.mapper.UserMapper;
import com.kfu.crossplatform.dto.UserDTO;
import com.kfu.crossplatform.exeptions.ResourceNotFoundException;
import com.kfu.crossplatform.domain.User;
import com.kfu.crossplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + "not found"));
                return user;
    }

    public List<UserDTO> getUsers(){
        return userRepository.findAll().stream().map(UserMapper::userToUserDTO).toList(); 
    }
    
    public UserDTO getUserDto(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
                return UserMapper.userToUserDTO(user);
    }

    public UserDTO getUserDto(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + "not found"));
                return UserMapper.userToUserDTO(user);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}

