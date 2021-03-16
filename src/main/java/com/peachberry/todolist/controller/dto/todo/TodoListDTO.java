package com.peachberry.todolist.controller.dto.todo;

import com.peachberry.todolist.domain.Todo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class TodoListDTO {

    private List<Todo> todoList;

    public TodoListDTO() {
        super();
    }

    public TodoListDTO(List<Todo> todoList) {
        this.todoList = todoList;
    }
}
