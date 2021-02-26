package com.peachberry.todolist.controller;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.dto.CategoryDTO;
import com.peachberry.todolist.dto.CategoryDeleteDTO;
import com.peachberry.todolist.dto.CategoryListDTO;
import com.peachberry.todolist.dto.CategoryUpdateDTO;
import com.peachberry.todolist.dto.response.SuccessResponseDTO;
import com.peachberry.todolist.service.CategoryService;
import com.peachberry.todolist.service.exception.CategoryUpdateFail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/{id}/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping("/save")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long id) {

        categoryService.saveCategory(id, categoryDTO);

        return ResponseEntity.ok(SuccessResponseDTO.builder().response("Save category success").build());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void CategorySaveFail(IllegalStateException e) {
        logger.error(e.getMessage());
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> findAllCategory(@PathVariable Long id) {

        CategoryListDTO categoryListDTO = new CategoryListDTO(categoryService.findAll(id));

        return ResponseEntity.ok(categoryListDTO);
    }


    @GetMapping("/search")
    public ResponseEntity<?> findCategoryTitle(@RequestParam("title") String title, @PathVariable Long id) {

        Category category = categoryService.findByTitle(title, id);

        return ResponseEntity.ok(CategoryDTO.builder().title(category.getTitle()).build());
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTitle(@Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO, @PathVariable Long id) {

        categoryService.reviseTitle(categoryUpdateDTO.getChangedTitle(), categoryUpdateDTO.getId());

        return ResponseEntity.ok(SuccessResponseDTO.builder().response("Update Complete").build());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void updateFailed(CategoryUpdateFail e) {
        logger.error(e.getMessage());
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteCategory(@Valid @RequestBody CategoryDeleteDTO categoryDeleteDTO, @PathVariable Long id) {

        categoryService.deleteCategory(categoryDeleteDTO.getId());

        return ResponseEntity.ok(SuccessResponseDTO.builder().response("Update Complete").build());
    }
}
