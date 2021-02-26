package com.peachberry.todolist.dto;

import lombok.Getter;

@Getter
public class CategoryUpdateDTO {

    private Long id;

    private String changedTitle;

    public CategoryUpdateDTO(Long id, String changedTitle) {
        this.id = id;
        this.changedTitle = changedTitle;
    }

    protected CategoryUpdateDTO() {}
}
