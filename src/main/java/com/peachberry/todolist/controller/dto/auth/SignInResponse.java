package com.peachberry.todolist.controller.dto.auth;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInResponse {

    private Boolean login;

    public SignInResponse(Boolean login) {
        this.login = login;
    }
}
