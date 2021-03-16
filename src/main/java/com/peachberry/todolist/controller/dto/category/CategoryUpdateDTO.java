package com.peachberry.todolist.controller.dto.category;

import com.peachberry.todolist.service.dto.category.CategoryServiceDto;
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

    public CategoryServiceDto.UpdateTitle toServiceDto() {
        return new CategoryServiceDto.UpdateTitle(id, changedTitle);
    }
}
