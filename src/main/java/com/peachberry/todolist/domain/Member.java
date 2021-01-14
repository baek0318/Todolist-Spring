package com.peachberry.todolist.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private Long password;

    @Column(name = "member_name")
    private String name;

    @OneToMany(mappedBy = "member")
    private List<Todo> todos;
}
