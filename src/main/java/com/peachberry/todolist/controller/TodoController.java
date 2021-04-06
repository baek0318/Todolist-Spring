package com.peachberry.todolist.controller;

import com.peachberry.todolist.controller.dto.TodoRequest;
import com.peachberry.todolist.controller.dto.TodoResponse;
import com.peachberry.todolist.domain.Calendar;
import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import com.peachberry.todolist.controller.dto.todo.TodoListDTO;
import com.peachberry.todolist.controller.dto.todo.TodoDTO;
import com.peachberry.todolist.controller.dto.SuccessResponseDTO;
import com.peachberry.todolist.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/todo/{member-id}")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    private Logger logger = LoggerFactory.getLogger(TodoController.class);

    @PostMapping
    public ResponseEntity<?> save(
            @Valid @RequestBody TodoRequest.Save saveDto,
            @PathVariable(name = "member-id") Long memberId)
    {

        Long result = todoService.saveTodo(saveDto.toEntity(), memberId, saveDto.getCategoryId());

        return ResponseEntity.ok(new TodoResponse.Save(result));
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
    public ResponseEntity<?> getTodoByCalendar(@RequestParam("calendar") Calendar calendar, @PathVariable Long memberId) {

        List<Todo> response = todoService.findTodoByCalendar(LocalDateTime.now(), memberId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/status")
    public ResponseEntity<?> getTodoByStatus(@RequestParam("status") String status, @PathVariable Long memberId) {
        TodoStatus todoStatus = TodoStatus.valueOf(status);
        List<Todo> response = todoService.findTodoByStatus(todoStatus, memberId);
        return ResponseEntity.ok().build();
    }
/*
    @PostMapping("/update/todo")
    public ResponseEntity<?> updateTodoTitle(@RequestBody TodoUpdateTitleDTO updateTitleDTO) {

        todoService.reviseTodoByTitle();
    }

    @PostMapping("/update/status")
    public ResponseEntity<?> updateTodoStatus(@RequestBody TodoUpdateStatusDTO updateStatusDTO) {
        todoService.reviseTodoByStatus();
    }

    @PostMapping("/update/Calendar")
    public ResponseEntity<?> updateTodoCalendar(@RequestBody TodoUpdateCalendarDTO updateCalendarDTO) {
        todoService.reviseTodoByCalendar();
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteTodo(@RequestParam("todoId") Long todoId, @PathVariable Long memberId) {
        todoService.deleteTodo(todoId);
    }

 */

}
