package com.company.exception;

public class TokenAlreadyConfirmedException extends RuntimeException{
    public TokenAlreadyConfirmedException(String message){
        super(message);
    }
}
