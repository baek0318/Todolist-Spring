package com.peachberry.todolist.dto;

import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryDTO {

    private Category category;

    private Member member;
}
