package com.company.controller;

import com.company.exception.ProfileCreationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ControllerExceptionHandler {

    @ExceptionHandler(value = {ProfileCreationException.class})
    public ResponseEntity<?> handleException(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
