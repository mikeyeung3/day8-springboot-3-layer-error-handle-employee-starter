package com.example.demo.exception;

public class InvaildSalaryForEmployeeAgeGreaterThan29 extends RuntimeException {
    public InvaildSalaryForEmployeeAgeGreaterThan29(String message) {
        super(message);
    }
}
