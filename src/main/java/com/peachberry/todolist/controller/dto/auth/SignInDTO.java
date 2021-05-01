package com.peachberry.todolist.controller.dto.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInDTO {

    private String email;

    private String password;

    public SignInDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
