package com.peachberry.todolist.controller.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInDTO {

    private final String email;

    private final String password;

    public SignInDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
