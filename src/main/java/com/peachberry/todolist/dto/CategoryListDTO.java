package com.peachberry.todolist.dto;

import com.peachberry.todolist.domain.Category;

import java.util.List;

public class CategoryListDTO {

    private List<Category> categoryList;

    public CategoryListDTO(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public CategoryListDTO() {}
}
