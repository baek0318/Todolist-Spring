package com.peachberry.todolist.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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

    private LocalDateTime dateTime;

    private String title;

    @Enumerated(EnumType.STRING)
    private TodoStatus status; //COMPLELETE, ING

    @Builder
    public Todo(Member member, Category category, LocalDateTime dateTime, String title, TodoStatus status) {
        this.member = member;
        this.category = category;
        this.dateTime = dateTime;
        this.title = title;
        this.status = status;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void changeStatus(TodoStatus status) {
        this.status = status;
    }
}
