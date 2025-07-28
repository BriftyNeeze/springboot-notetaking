package com.example.note_taking.models;

public enum UserRole {
    USER,
    ADMIN;

     public String withPrefix() {
        return this.name();
    }
}
