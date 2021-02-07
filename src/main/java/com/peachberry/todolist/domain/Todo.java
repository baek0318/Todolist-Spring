package com.peachberry.todolist.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

    @NotBlank
    private String title;

    @Enumerated(EnumType.STRING)
    private TodoStatus status; //COMPLELETE, ING

    @Builder
    public Todo(Member member, Category category, Calendar calendar, @NotBlank String title, TodoStatus status) {
        this.member = member;
        this.category = category;
        this.calendar = calendar;
        this.title = title;
        this.status = status;
    }

    protected Todo() {}

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void changeStatus(TodoStatus status) {
        this.status = status;
    }
}
