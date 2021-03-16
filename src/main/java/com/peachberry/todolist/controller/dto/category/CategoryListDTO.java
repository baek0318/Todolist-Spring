package com.peachberry.todolist.controller.dto.category;

import com.peachberry.todolist.domain.Category;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoryListDTO {

    private List<Category> categoryList;

    public CategoryListDTO(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public CategoryListDTO() {}
}
