package com.peachberry.todolist.service.exception;

public class SignInFailException extends RuntimeException {

    public SignInFailException(String message) {
        super(message);
    }
}
