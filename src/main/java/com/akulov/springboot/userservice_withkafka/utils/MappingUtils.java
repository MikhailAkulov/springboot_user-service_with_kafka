package com.akulov.springboot.userservice_withkafka.utils;

import com.akulov.springboot.userservice_withkafka.dto.UserDto;
import com.akulov.springboot.userservice_withkafka.entity.User;

public class MappingUtils {
    // из entity в dto
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());
        userDto.setCreatedAt(user.getCreatedAt());
        return userDto;
    }

    // из dto в entity
    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        return user;
    }

    public static void updateUser(User user, UserDto userDto) {
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getAge() != null) {
            user.setAge(userDto.getAge());
        }
    }
}
