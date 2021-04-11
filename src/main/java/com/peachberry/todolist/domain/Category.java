package com.peachberry.todolist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.peachberry.todolist.controller.dto.CategoryResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String title;

    @OneToMany(mappedBy = "category")
    private List<Todo> todos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Category(String title, Member member) {
        this.title = title;
        this.member = member;
    }

    public CategoryResponse.CategoryInfo toInfoResponse() {
        return new CategoryResponse.CategoryInfo(
                this.id,
                this.title
        );
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
