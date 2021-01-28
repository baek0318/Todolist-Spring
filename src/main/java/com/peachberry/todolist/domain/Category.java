package com.peachberry.todolist.domain;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @NotBlank
    private String title;

    @OneToMany(mappedBy = "category")
    private List<Todo> todos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Category(@NotBlank String title, Member member) {
        this.title = title;
        this.member = member;
    }

    protected Category() {

    }

    public void changeTitle(String title) {
        this.title = title;
    }
}
