package com.akulov.springboot.userservice_withkafka.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class UserDto {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 25)
    private String name;

    @Email
    @NotBlank
    private String email;

    @Min(1)
    @Max(110)
    private Integer age;

    private LocalDateTime createdAt;

    public UserDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
