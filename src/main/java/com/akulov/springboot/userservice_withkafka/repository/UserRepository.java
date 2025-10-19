package com.akulov.springboot.userservice_withkafka.repository;

import com.akulov.springboot.userservice_withkafka.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByName(String name);
}
