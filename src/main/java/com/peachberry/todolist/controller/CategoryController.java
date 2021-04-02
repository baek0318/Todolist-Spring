package com.peachberry.todolist.controller;

import com.peachberry.todolist.controller.dto.CategoryControllerDto;
import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.service.CategoryService;
import com.peachberry.todolist.service.dto.CategoryServiceDto;
import com.peachberry.todolist.service.exception.CategoryUpdateFail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/category/{member-id}")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping("")
    public ResponseEntity<?> saveCategory(
            @Valid @RequestBody CategoryControllerDto.Save saveDto,
            @PathVariable(name = "member-id") Long id
    )
    {
        categoryService.saveCategory(saveDto.toServiceDto(id));

        return ResponseEntity.ok(SuccessResponseDTO.builder().response("Save category success").build());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void CategorySaveFail(IllegalStateException e) {
        logger.error(e.getMessage());
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllCategory(@PathVariable(name = "member-id") Long id) {

        CategoryControllerDto.CategoryList categoryListDTO = new CategoryControllerDto.CategoryList(categoryService.findAll(id));

        return ResponseEntity.ok(categoryListDTO);
    }


    @GetMapping("")
    public ResponseEntity<?> findCategoryTitle(
            @RequestParam("title") String title,
            @PathVariable(name = "member-id") Long id
    )
    {

        Category category = categoryService.findByTitle(new CategoryServiceDto.FindByTitle(id, title));

        return ResponseEntity.ok(category.toInfoResponse());
    }

    @PatchMapping("")
    public ResponseEntity<?> updateTitle(
            @Valid @RequestBody CategoryControllerDto.Update updateDto,
            @PathVariable(name = "member-id") Long id
    )
    {

        categoryService.reviseTitle(updateDto.toServiceDto());

        return ResponseEntity.ok(SuccessResponseDTO.builder().response("Update Complete").build());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void updateFailed(CategoryUpdateFail e) {
        logger.error(e.getMessage());
    }

    @DeleteMapping("/{category-id}")
    public ResponseEntity<?> deleteCategory(
            @Valid @RequestBody CategoryControllerDto.Delete deleteDto,
            @PathVariable(name = "member-id") Long memberId,
            @PathVariable(name = "category-id") Long categoryId
    )
    {

        categoryService.deleteCategory(deleteDto.getId());

        return ResponseEntity.ok(SuccessResponseDTO.builder().response("Update Complete").build());
    }
}
