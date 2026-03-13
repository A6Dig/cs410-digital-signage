package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.UserRequestDto;
import com.a6dig.digitalsignage.dto.UserResponseDto;
import com.a6dig.digitalsignage.entity.User;
import com.a6dig.digitalsignage.exception.UserExistsException;
import com.a6dig.digitalsignage.repository.UserRepository;

public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new UserExistsException("Email " +user.getEmail() + " already exists.");
        });
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        userRepository.save(user);

        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());

        return response;
    }
}
