package com.peachberry.todolist.dto.response;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpSuccessDTO {

    private final String email;

    private final String name;

    private final Role role;

    private final Long id;

}
