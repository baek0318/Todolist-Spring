package com.peachberry.todolist.controller.dto.todo;

import com.peachberry.todolist.domain.Calendar;
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
