package com.peachberry.todolist.dto.request;

import com.peachberry.todolist.domain.Calendar;
import com.peachberry.todolist.domain.Category;
import com.peachberry.todolist.domain.Member;
import com.peachberry.todolist.domain.TodoStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoDTO {

    private Long id;

    private Member member;

    private Category category;

    private Calendar calendar;

    private String title;

    private TodoStatus status;

}
