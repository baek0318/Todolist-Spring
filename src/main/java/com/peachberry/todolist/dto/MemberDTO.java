package com.peachberry.todolist.dto;

import com.peachberry.todolist.domain.Authority;
import com.peachberry.todolist.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MemberDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 4)
    private String password;

    @NotBlank
    private String name;

    private Authority authority;

    public MemberDTO(@Email @NotBlank String email, @NotBlank @Size(min = 4) String password, @NotBlank String name, Authority authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.authority = authority;
    }

    public Member toEntity() {
        return Member.builder()
                .authority(authority)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    public Member toSecurityEntity() {
        return Member.builder()
                .authority(authority)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    public
}
