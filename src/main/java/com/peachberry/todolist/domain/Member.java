package com.peachberry.todolist.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private long password;

    @Column(name = "member_name")
    private String name;

    @ManyToOne
    @JoinColumn("authority_id")
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();
}
