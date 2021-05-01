package com.peachberry.todolist.controller.dto.auth;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpSuccessDTO {

    private String email;

    private String name;

    private Role role;

    private Long id;

    public SignUpSuccessDTO(String email, String name, Role role, Long id) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.id = id;
    }
}
