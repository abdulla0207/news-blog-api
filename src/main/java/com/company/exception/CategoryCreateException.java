package com.company.exception;

public class CategoryCreateException extends RuntimeException{
    public CategoryCreateException(String message){
        super(message);
    }
}
