package com.peachberry.todolist.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 20)
    private String password;

    @Column(name = "member_name")
    @NotBlank
    private String name;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Category> categories = new ArrayList<>();

    public Member(String email, String password, String name, Authority authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.authority = authority;
    }

    protected Member() {

    }
}
