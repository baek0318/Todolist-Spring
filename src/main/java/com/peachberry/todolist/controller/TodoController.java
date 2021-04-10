package com.peachberry.todolist.controller;

import com.peachberry.todolist.controller.dto.TodoRequest;
import com.peachberry.todolist.controller.dto.TodoResponse;
import com.peachberry.todolist.domain.Todo;
import com.peachberry.todolist.domain.TodoStatus;
import com.peachberry.todolist.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Path;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/{member-id}")
    public ResponseEntity<TodoResponse.Save> save(
            @Valid @RequestBody TodoRequest.Save saveDto,
            @PathVariable(name = "member-id") Long memberId)
    {

        Long result = todoService.saveTodo(saveDto.toEntity(), memberId, saveDto.getCategoryId());

        return ResponseEntity.ok(new TodoResponse.Save(result));
    }

    @GetMapping("{member-id}/all")
    public ResponseEntity<TodoResponse.TodoInfoList> getAllTodo(@PathVariable(name = "member-id") Long id) {

        List<Todo> todoList = todoService.findAllTodo(id);

        return ResponseEntity.ok(new TodoResponse.TodoInfoList(toTodoInfoList(todoList)));
    }

    @GetMapping("{member-id}/{todo-id}")
    public ResponseEntity<TodoResponse.TodoInfo> getTodoById(
            @PathVariable(name = "todo-id") Long todoId
    ) {

        Todo todo = todoService.findTodoById(todoId);

        return ResponseEntity.ok(new TodoResponse.TodoInfo(todo));
    }


    @GetMapping("/{member-id}")
    public ResponseEntity<TodoResponse.TodoInfoList> getTodoByParam(
            @RequestParam Map<String, String> param,
            @PathVariable(name = "member-id") Long memberId)
    {
        List<TodoResponse.TodoInfo> result = new ArrayList<>();

        if(param.get("status") != null) {
            List<Todo> todoList = todoService.findTodoByStatus(
                    TodoStatus.valueOf(param.get("status")),
                    memberId);
            result = toTodoInfoList(todoList);
        }
        if (param.get("datetime") != null) {
            List<Todo> todoList = todoService.findTodoByCalendar(
                    LocalDate.parse(
                            param.get("datetime"),
                            DateTimeFormatter.ISO_LOCAL_DATE
                    ),
                    memberId);
            result = toTodoInfoList(todoList);
        }

        return ResponseEntity.ok(new TodoResponse.TodoInfoList(result));
    }

    private List<TodoResponse.TodoInfo> toTodoInfoList(List<Todo> todoList) {
        return todoList
                .stream()
                .map(TodoResponse.TodoInfo::new)
                .collect(Collectors.toList());
    }

    @PutMapping("")
    public ResponseEntity<TodoResponse.Update> updateTodoTitle(
            @Valid @RequestBody TodoRequest.Update updateDto) {

        Long result = todoService.updateTodo(updateDto.toEntity(), updateDto.getCategory().getId());

        return ResponseEntity.ok(new TodoResponse.Update(result));
    }
    /*
    @GetMapping("/delete")
    public ResponseEntity<?> deleteTodo(@RequestParam("todoId") Long todoId) {
        todoService.deleteTodo(todoId);

        return ResponseEntity.ok();
    }

     */


}
