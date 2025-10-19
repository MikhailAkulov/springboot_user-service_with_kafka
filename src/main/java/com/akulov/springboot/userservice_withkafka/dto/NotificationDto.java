package com.akulov.springboot.userservice_withkafka.dto;

import com.akulov.springboot.userservice_withkafka.enums.OperationType;

public class NotificationDto {
    private OperationType operationType;
    private String email;
    private String userName;

    public NotificationDto(OperationType operationType, String email, String userName) {
        this.operationType = operationType;
        this.email = email;
        this.userName = userName;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
