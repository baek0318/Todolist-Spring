package com.peachberry.todolist.dto;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import lombok.Builder;
import lombok.Getter;


@Getter
public class CategoryDTO {

    private String title;

    @Builder
    public CategoryDTO(String title) {
        this.title = title;
    }

    public CategoryDTO() {}
}
