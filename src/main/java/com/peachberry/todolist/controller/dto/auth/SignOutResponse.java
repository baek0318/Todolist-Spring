package com.peachberry.todolist.controller.dto.auth;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignOutResponse {

    private Boolean login;

    public SignOutResponse(Boolean login) {
        this.login = login;
    }
}
