package com.peachberry.todolist.dto;

import lombok.Getter;

@Getter
public class CategoryDeleteDTO {
    private Long id;

    private String changedTitle;

    public CategoryDeleteDTO(Long id, String changedTitle) {
        this.id = id;
        this.changedTitle = changedTitle;
    }

    protected CategoryDeleteDTO() {}
}
