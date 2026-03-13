package com.a6dig.digitalsignage.controller;

import com.a6dig.digitalsignage.dto.UserRequestDto;
import com.a6dig.digitalsignage.dto.UserResponseDto;
import com.a6dig.digitalsignage.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto dto){
        return userService.createUser(dto);
    }
}
