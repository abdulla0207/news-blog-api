package com.company.controller;

import com.company.exception.ProfileCreateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ControllerExceptionHandler {

    @ExceptionHandler(value = {ProfileCreateException.class})
    public ResponseEntity<?> handleException(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
