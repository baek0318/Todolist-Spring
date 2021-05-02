package com.peachberry.todolist.controller.dto.auth;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpResponse {

    private Long id;

    public SignUpResponse(Long id) {
        this.id = id;
    }
}