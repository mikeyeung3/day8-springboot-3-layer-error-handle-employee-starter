package com.example.demo.controller.advice;

import com.example.demo.exception.InvaildSalaryForEmployeeAgeGreaterThan29Exception;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidOperationOnInactiveEmployeeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException exceptionHandler(Exception e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler({InvalidAgeEmployeeException.class, InvalidOperationOnInactiveEmployeeException.class, InvaildSalaryForEmployeeAgeGreaterThan29Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException invalidAgeEmployeeExceptionHandler(Exception e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseException handlerArgumentNotValid(MethodArgumentNotValidException exception){
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" | "));

        return new ResponseException(errorMessage);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseException generalExceptionHandler(Exception e) {
        return new ResponseException("An unexpected error occurred: " + e.getMessage());
    }
}
