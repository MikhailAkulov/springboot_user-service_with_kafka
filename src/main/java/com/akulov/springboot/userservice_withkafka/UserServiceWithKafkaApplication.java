package com.akulov.springboot.userservice_withkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class UserServiceWithKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceWithKafkaApplication.class, args);
    }
}
