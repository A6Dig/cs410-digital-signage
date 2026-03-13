package com.a6dig.digitalsignage.UnitTest.service;

import com.a6dig.digitalsignage.dto.UserRequestDto;
import com.a6dig.digitalsignage.dto.UserResponseDto;
import com.a6dig.digitalsignage.entity.User;
import com.a6dig.digitalsignage.repository.UserRepository;
import com.a6dig.digitalsignage.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void createUser() {
        UserRequestDto dto = new UserRequestDto();
        dto.setUsername("johndoe");
        dto.setEmail("john.doe@email.com");
        dto.setPassword("pass");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("johndoe");
        savedUser.setEmail("john.doe@email.com");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDto response = userService.createUser(dto);
        assertEquals("johndoe", response.getUsername());
        assertEquals("john.doe@email.com", response.getEmail());
    }
}