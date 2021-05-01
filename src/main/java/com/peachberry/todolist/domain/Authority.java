package com.peachberry.todolist.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @OneToMany(mappedBy = "authority")
    private List<Member> members = new ArrayList<>();

    public Authority(Role role) {
        this.role = role;
    }

    public Authority(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    @Override
    public String toString() {
        return id + " " + role;
    }

    @Override
    public boolean equals(Object object) {
        Authority authority = (Authority) object;
        return role == authority.role;
    }
}
