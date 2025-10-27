package com.akulov.springboot.userservice_withkafka.controller;

import com.akulov.springboot.userservice_withkafka.dto.UserDto;
import com.akulov.springboot.userservice_withkafka.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> showAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.findUserById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto newUserDto = userService.addUser(userDto);
        return ResponseEntity
                .created(URI.create("/api/users/" + newUserDto.getId()))
                .body(newUserDto);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        try {
            UserDto updatedUserDto = userService.updateUser(id, userDto);
            return ResponseEntity.ok(updatedUserDto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/name/{name}")
    public List<UserDto> showAllUsersByName(@PathVariable String name) {
        return userService.findAllUsersByName(name);
    }
}
