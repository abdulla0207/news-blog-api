package com.company.exception;

// This exception is thrown when the object in database is not found
// Mainly used in service classes.
public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(String message){
        super(message);
    }
}
