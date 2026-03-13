package com.a6dig.digitalsignage.UnitTest.controller;

import com.a6dig.digitalsignage.controller.UserController;
import com.a6dig.digitalsignage.dto.UserResponseDto;
import com.a6dig.digitalsignage.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;



    @Test
    void createUser() throws Exception{
        UserResponseDto response = new UserResponseDto();

        response.setId(1L);
        response.setUsername("johndoe");
        response.setEmail("john.doe@email.com");

        when(userService.createUser(any())).thenReturn(response);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(
                "{\"username\":\"johndoe\"" +
                        ",\"email\":\"john.doe@email.com\"}"
        )).andExpect(status().isOk());
    }
}