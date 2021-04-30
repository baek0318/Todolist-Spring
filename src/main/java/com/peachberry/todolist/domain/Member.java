package com.peachberry.todolist.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    @Size(min = 4)
    private String password;

    @Column(name = "member_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Category> categories = new ArrayList<>();

    @Builder
    public Member(String email, String password, String name, Authority authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.authority = authority;
    }

   public void setEncryptPassword(String password) {
        this.password = password;
   }

   public void setAuthority(Authority authority) {
        this.authority = authority;
   }
}
