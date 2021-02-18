package com.peachberry.todolist.controller;

import com.peachberry.todolist.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory() {
        //categoryService.saveCategory();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/search/title")
    public ResponseEntity<?> findCategoryTitle() {
        //categoryService.findByTitle();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/seach/all")
    public ResponseEntity<?> findAllCategory() {
        //categoryService.findAll();

        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCategory() {
        //categoryService.deleteCategory();

        return ResponseEntity.ok().build();
    }
}
