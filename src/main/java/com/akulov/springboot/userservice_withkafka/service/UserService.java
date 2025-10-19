package com.akulov.springboot.userservice_withkafka.service;

import com.akulov.springboot.userservice_withkafka.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();
    UserDto findUserById(Long id);
    UserDto addUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUserById(Long id);
    List<UserDto> findAllUsersByName(String name);
}
