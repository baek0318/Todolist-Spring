package com.peachberry.todolist.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Embedded
    private Calendar calendar;

    @Enumerated(EnumType.STRING)
    private TodoStatus status; //COMPLELETE, ING
}
