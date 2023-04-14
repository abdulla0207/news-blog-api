package com.company.controller;

import com.company.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {ProfileCreateException.class,
            ItemNotFoundException.class,
            EmailException.class,
            ArticleCreateException.class,
            ItemAlreadyExistsException.class})
    public ResponseEntity<?> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({TokenNotValidException.class})
    public ResponseEntity<?> handleUnauthorized(TokenNotValidException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler({AppForbiddenException.class})
    public ResponseEntity<?> handleForbidden(AppForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
