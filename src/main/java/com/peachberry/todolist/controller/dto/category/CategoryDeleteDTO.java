package com.peachberry.todolist.controller.dto.category;

import lombok.Getter;

@Getter
public class CategoryDeleteDTO {
    private Long id;

    public CategoryDeleteDTO(Long id) {
        this.id = id;
    }

    protected CategoryDeleteDTO() {}
}
