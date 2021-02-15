package com.peachberry.todolist.service.exception;

public class SignUpFailException extends RuntimeException {

    public SignUpFailException(String message) {
        super(message);
    }
}
