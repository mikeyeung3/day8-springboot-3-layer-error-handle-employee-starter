package com.example.demo.exception;

public class InvalidOperationOnInactiveEmployeeException extends RuntimeException {
    public InvalidOperationOnInactiveEmployeeException(String message) {
        super(message);
    }
}
