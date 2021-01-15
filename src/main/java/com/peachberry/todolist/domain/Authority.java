package com.peachberry.todolist.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Authority {

    @Id
    @GeneratedValue
    @Column(name = "authority_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "authority")
    private List<Member> members = new ArrayList<>();
}
