package com.peachberry.todolist.dto;

import com.peachberry.todolist.domain.Authority;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpSuccessDTO {

    private final String email;

    private final String name;

    private final Authority authority;

    private final Long id;

}
