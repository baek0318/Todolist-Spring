package com.peachberry.todolist.dto.request;

import com.peachberry.todolist.domain.Authority;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpDTO {

    private final String email;

    private final String password;

    private final String authority;

    private final String name;

    public SignUpDTO(String email, String password, String name, String authority) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.authority = authority;
    }
}
