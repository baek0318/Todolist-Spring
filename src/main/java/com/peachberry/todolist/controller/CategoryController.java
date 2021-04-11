package com.peachberry.todolist.controller;

import com.peachberry.todolist.controller.dto.CategoryControllerDto;
import com.peachberry.todolist.controller.dto.CategoryResponse;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.service.CategoryService;
import com.peachberry.todolist.service.dto.CategoryServiceDto;
import com.peachberry.todolist.service.exception.CategoryUpdateFail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category/{member-id}")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping
    public ResponseEntity<CategoryResponse.Save> saveCategory(
            @Valid @RequestBody CategoryControllerDto.Save saveDto,
            @PathVariable(name = "member-id") Long id
    )
    {
        Long result = categoryService.saveCategory(saveDto.toServiceDto(id));

        return ResponseEntity.ok(new CategoryResponse.Save(result));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void CategorySaveFail(IllegalStateException e) {
        logger.error(e.getMessage());
    }

    @GetMapping("/all")
    public ResponseEntity<CategoryResponse.CategoryList> findAllCategory(@PathVariable(name = "member-id") Long id) {

        List<CategoryResponse.CategoryInfo> result = categoryService.findAll(id)
                .stream()
                .map(it -> new CategoryResponse.CategoryInfo(it.getId(), it.getTitle()))
                .collect(Collectors.toList());

        CategoryResponse.CategoryList categoryListDTO = new CategoryResponse.CategoryList(result);

        return ResponseEntity.ok(categoryListDTO);
    }


    @GetMapping
    public ResponseEntity<CategoryResponse.CategoryInfo> findCategoryTitle(
            @RequestParam("title") String title,
            @PathVariable(name = "member-id") Long id
    )
    {

        Category category = categoryService.findByTitle(new CategoryServiceDto.FindByTitle(id, title));

        return ResponseEntity.ok(category.toInfoResponse());
    }

    @PutMapping
    public ResponseEntity<CategoryResponse.Update> updateTitle(
            @Valid @RequestBody CategoryControllerDto.Update updateDto
    )
    {

        Long resultId = categoryService.reviseTitle(updateDto.toServiceDto());

        return ResponseEntity.ok(new CategoryResponse.Update(resultId));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void updateFailed(CategoryUpdateFail e) {
        logger.error(e.getMessage());
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponseDTO> deleteCategory(
            @Valid @RequestBody CategoryControllerDto.Delete deleteDto
    )
    {

        categoryService.deleteCategory(deleteDto.getId());

        return ResponseEntity.ok(new SuccessResponseDTO("DELETE"));
    }
}
