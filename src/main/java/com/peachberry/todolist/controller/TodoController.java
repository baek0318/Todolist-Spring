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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo/{member-id}")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    private Logger logger = LoggerFactory.getLogger(TodoController.class);

    @PostMapping
    public ResponseEntity<TodoResponse.Save> save(
            @Valid @RequestBody TodoRequest.Save saveDto,
            @PathVariable(name = "member-id") Long memberId)
    {

        Long result = todoService.saveTodo(saveDto.toEntity(), memberId, saveDto.getCategoryId());

        return ResponseEntity.ok(new TodoResponse.Save(result));
    }

    @GetMapping("/all")
    public ResponseEntity<TodoResponse.TodoInfoList> getAllTodo(@PathVariable(name = "member-id") Long id) {

        List<TodoResponse.TodoInfo> todoList = todoService.findAllTodo(id)
                .stream()
                .map(TodoResponse.TodoInfo::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new TodoResponse.TodoInfoList(todoList));
    }

    @GetMapping("")
    public ResponseEntity<?> getTodoByParam(
            @RequestParam Map<String, String> param,
            @PathVariable Long memberId)
    {
        System.out.println("=============="+param.get("status"));

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
