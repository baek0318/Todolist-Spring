package com.peachberry.todolist.controller.dto.auth;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
public class SignUpDTO {

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    @Size(min = 4)
    private final String password;

    @NotBlank
    private final String authority;

    @NotBlank
    private final String name;

    public SignUpDTO(String email, String password, String name, String authority) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.authority = authority;
    }
}
