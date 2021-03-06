package com.peachberry.todolist.controller;

import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.dto.TodoListDTO;
import com.peachberry.todolist.dto.request.TodoDTO;
import com.peachberry.todolist.dto.response.SuccessResponseDTO;
import com.peachberry.todolist.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/{id}/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    private Logger logger = LoggerFactory.getLogger(TodoController.class);

    @PostMapping("/save")
    public ResponseEntity<?> save(@Valid @RequestBody TodoDTO todoDTO, @PathVariable String id) {

        todoService.saveTodo(todoDTO);

        return ResponseEntity.ok(SuccessResponseDTO.builder().response("Save todo success").build());
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> getAllTodo(@PathVariable String id) {

        List<Todo> todoList =  todoService.findAllTodo(Long.parseLong(id));

        return ResponseEntity.ok(new TodoListDTO(todoList));
    }

    @GetMapping("/search/cateogry")
    public ResponseEntity<?> getTodoByCategory(@RequestParam("category_id") Long category_id, @PathVariable Long member_id) {

        List<Todo> response = todoService.findTodoByCategory(category_id, member_id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/calendar")
    public ResponseEntity<?> getTodoByCalendar() {

    }

    @GetMapping("/search/status")
    public ResponseEntity<?> getTodoByStatus() {

    }

    @PostMapping("/update/todo")
    public ResponseEntity<?> updateTodoTitle() {

    }

    @PostMapping("/update/status")
    public ResponseEntity<?> updateTodoStatus() {

    }

    @PostMapping("/update/Calendar")
    public ResponseEntity<?> updateTodoCalendar() {

    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteTodo() {

    }

}
