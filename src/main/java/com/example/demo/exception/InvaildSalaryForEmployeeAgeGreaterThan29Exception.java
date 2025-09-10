package com.example.demo.exception;

public class InvaildSalaryForEmployeeAgeGreaterThan29Exception extends RuntimeException {
    public InvaildSalaryForEmployeeAgeGreaterThan29Exception(String message) {
        super(message);
    }
}
