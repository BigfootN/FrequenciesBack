package com.frequencies.logs.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Document(collection = "logs")
public class Log {
    @Id
    private ObjectId id;

    private String scope;

    private Severity severity;

    private String message;

    private LocalDateTime date;

    public ObjectId getId() {
        return id;
    }

    public void setId(@NonNull final ObjectId id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(@NonNull final String scope) {
        this.scope = scope;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(@NonNull final Severity severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull final String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(@NonNull final LocalDateTime date) {
        this.date = date;
    }
}
