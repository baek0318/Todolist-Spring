package com.peachberry.todolist.dto.request;

import com.peachberry.todolist.domain.Calendar;
import com.peachberry.todolist.dto.CategoryDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoDTO {

    private String category;

    private Calendar calendar;

    private String title;

    private Long member_id;
}
