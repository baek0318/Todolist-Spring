package com.peachberry.todolist.service;

import com.peachberry.todolist.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void save() {

    }

    public void findByTitle() {

    }

    public void reviseTitle() {

    }

    public void deleteCategory() {

    }
}
