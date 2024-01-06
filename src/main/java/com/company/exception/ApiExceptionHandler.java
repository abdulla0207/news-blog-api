package com.company.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ArticleCreateException.class,
            CategoryCreateException.class,
            ProfileCreateException.class,})
    public ResponseEntity<ExceptionResponse> handleBadRequestException(RuntimeException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ItemNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(RuntimeException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ItemAlreadyExistsException.class, EmailException.class})
    public ResponseEntity<ExceptionResponse> handleAlreadyExistException(RuntimeException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {AppForbiddenException.class})
    public ResponseEntity<ExceptionResponse> handleForbiddenException(RuntimeException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(),
                HttpStatus.FORBIDDEN,
                ZonedDateTime.now());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

}
