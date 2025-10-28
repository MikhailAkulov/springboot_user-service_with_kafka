package com.akulov.springboot.userservice_withkafka.service;

import com.akulov.springboot.userservice_withkafka.dto.NotificationDto;
import com.akulov.springboot.userservice_withkafka.enums.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @Value("${app.kafka.topic.user-notifications:user-notifications}")
    private String userNotificationsTopic;

    public KafkaProducerService(KafkaTemplate<String, NotificationDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(OperationType operationType, String email, String userName) {
        log.info("KafkaProducerService. Attempting to send notification about operation to email");
        try {
            NotificationDto notification = new NotificationDto(operationType, email, userName);

            kafkaTemplate.send(userNotificationsTopic, notification);
            log.info("Notification about operation: {} was successfully sent to email: {}", operationType, email);
        } catch (Exception e) {
            log.error("Failed to send notification about operation: {} to email: {}",  operationType, email ,e);
        }
    }
}
