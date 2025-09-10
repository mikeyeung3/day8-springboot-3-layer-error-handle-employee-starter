package com.example.demo.exception;

public class InvalidOperationOnInactiveEmployee extends RuntimeException {
    public InvalidOperationOnInactiveEmployee(String message) {
        super(message);
    }
}
