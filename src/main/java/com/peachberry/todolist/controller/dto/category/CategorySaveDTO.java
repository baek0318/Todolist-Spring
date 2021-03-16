package com.peachberry.todolist.controller.dto.category;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.service.dto.category.CategoryServiceDto;
import lombok.Builder;
import lombok.Getter;


@Getter
public class CategorySaveDTO {

    private String title;

    @Builder
    public CategorySaveDTO(String title) {
        this.title = title;
    }

    public CategorySaveDTO() {}

    public CategoryServiceDto.Save toServiceDto(Long memberId) {
        return new CategoryServiceDto.Save(memberId, this.title);
    }
}
