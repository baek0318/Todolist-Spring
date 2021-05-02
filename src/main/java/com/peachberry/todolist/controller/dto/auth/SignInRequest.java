package com.peachberry.todolist.controller.dto.auth;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {

    private String email;

    private String password;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}