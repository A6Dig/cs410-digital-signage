package com.a6dig.digitalsignage.service;

import com.a6dig.digitalsignage.dto.UserRequestDto;
import com.a6dig.digitalsignage.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDto dto);
}
