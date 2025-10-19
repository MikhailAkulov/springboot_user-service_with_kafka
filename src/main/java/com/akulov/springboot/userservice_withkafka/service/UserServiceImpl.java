package com.akulov.springboot.userservice_withkafka.service;

import com.akulov.springboot.userservice_withkafka.dto.UserDto;
import com.akulov.springboot.userservice_withkafka.entity.User;
import com.akulov.springboot.userservice_withkafka.enums.OperationType;
import com.akulov.springboot.userservice_withkafka.repository.UserRepository;
import com.akulov.springboot.userservice_withkafka.utils.MappingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    public UserServiceImpl(UserRepository userRepository, KafkaProducerService kafkaProducerService) {
        this.userRepository = userRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(MappingUtils::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long id) {
        return userRepository.findById(id)
                .map(MappingUtils::mapToUserDto)
                .orElseThrow(() -> new RuntimeException("User with specified ID not found"));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User newUser = MappingUtils.mapToUser(userDto);
        User savedUser = userRepository.save(newUser);

        kafkaProducerService.sendNotification(
                OperationType.CREATE,
                savedUser.getEmail(),
                savedUser.getName()
        );

        return MappingUtils.mapToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with specified ID not found"));
        MappingUtils.updateUser(user, userDto);
        return MappingUtils.mapToUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with specified ID not found"));

        String email = user.getEmail();
        String name = user.getName();
        userRepository.deleteById(id);
        kafkaProducerService.sendNotification(
                OperationType.DELETE,
                email,
                name
        );
    }

    @Override
    public List<UserDto> findAllUsersByName(String name) {;
        return userRepository.findAllByName(name).stream()
                .map(MappingUtils::mapToUserDto)
                .collect(Collectors.toList());
    }
}
