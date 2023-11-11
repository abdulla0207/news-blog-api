package com.company.exception;

// This exception is used when the object to be created already exists in db
public class ItemAlreadyExistsException extends RuntimeException{
    public ItemAlreadyExistsException(String message){
        super(message);
    }
}
