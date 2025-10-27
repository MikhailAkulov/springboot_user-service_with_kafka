package com.akulov.springboot.userservice_withkafka.controller;

import com.akulov.springboot.userservice_withkafka.dto.UserDto;
import com.akulov.springboot.userservice_withkafka.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setEmail("john@example.com");
        userDto.setAge(32);
        userDto.setCreatedAt(LocalDateTime.of(2025, 10, 12, 16, 24));
    }

    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.findAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void testGetUserByIdSuccess() throws Exception {
        Mockito.when(userService.findUserById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        Mockito.when(userService.findUserById(999L))
                .thenThrow(new RuntimeException("User with specified ID not found"));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddUser() throws Exception {
        UserDto toCreateDto = new UserDto();
        toCreateDto.setName("Dave");
        toCreateDto.setEmail("dave@example.com");
        toCreateDto.setAge(22);

        UserDto createdDto = new UserDto();
        createdDto.setId(2L);
        createdDto.setName("Dave");
        createdDto.setEmail("dave@example.com");
        createdDto.setAge(22);
        createdDto.setCreatedAt(LocalDateTime.now());

        Mockito.when(userService.addUser(any(UserDto.class))).thenReturn(createdDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/2"))
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        UserDto update = new UserDto();
        update.setName("Johnny");
        update.setEmail("johnny.mnemonic@mail.com");
        update.setAge(42);
        update.setId(1L);
        update.setCreatedAt(userDto.getCreatedAt());

        Mockito.when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(update);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Johnny"));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        UserDto update = new UserDto();
        update.setName("Johnny");
        update.setEmail("johnny.mnemonic@mail.com");
        update.setAge(42);
        update.setId(999L);
        update.setCreatedAt(userDto.getCreatedAt());

        Mockito.when(userService.updateUser(eq(999L), any(UserDto.class)))
                .thenThrow(new RuntimeException("User with specified ID not found"));

        mockMvc.perform(put("/api/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUserById(1L);
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("User with specified ID not found"))
                .when(userService).deleteUserById(999L);

        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllUsersByNameSuccess() throws Exception {
        Mockito.when(userService.findAllUsersByName("John")).thenReturn(List.of(userDto));

        mockMvc.perform(get("/api/users/name/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void testLengthOfListWhenGetAllUsersByName() throws Exception {
        UserDto secondJohn = new UserDto();
        secondJohn.setId(2L);
        secondJohn.setName("John");
        secondJohn.setEmail("johnny@example.com");
        secondJohn.setAge(19);
        secondJohn.setCreatedAt(LocalDateTime.now());

        Mockito.when(userService.findAllUsersByName("John")).thenReturn(List.of(userDto, secondJohn));

        mockMvc.perform(get("/api/users/name/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testEmptyListWhenGetAllUsersByNonExistingName() throws Exception {
        Mockito.when(userService.findAllUsersByName("Nemo")).thenReturn(List.of());

        mockMvc.perform(get("/api/users/name/Nemo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}