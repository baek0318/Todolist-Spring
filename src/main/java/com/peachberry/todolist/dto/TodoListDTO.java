package com.peachberry.todolist.dto;

import com.peachberry.todolist.domain.Todo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TodoListDTO {

    private List<Todo> todoList;

}
