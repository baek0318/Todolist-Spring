package com.peachberry.todolist.controller.dto.auth;

import com.peachberry.todolist.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 4)
    private String password;

    @NotBlank
    private String name;

    public SignUpDTO(String email, String password, String name) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .build();
    }
}
