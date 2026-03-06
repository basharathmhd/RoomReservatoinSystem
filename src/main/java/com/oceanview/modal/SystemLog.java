package com.oceanview.modal;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SystemLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String logId;
    private LocalDateTime timestamp;
    private String userId;
    private String action;
    private String description;
    private String ipAddress;
    private String entityType;
    private String entityId;

    // Constructors
    public SystemLog() {
        this.timestamp = LocalDateTime.now();
    }

    public SystemLog(String userId, String action, String description) {
        this();
        this.userId = userId;
        this.action = action;
        this.description = description;
    }

    // Getters and Setters
    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return "SystemLog{" +
                "logId='" + logId + '\'' +
                ", timestamp=" + timestamp +
                ", userId='" + userId + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
